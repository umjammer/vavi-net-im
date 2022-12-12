package javax.imps;
/**
 * Enable a user to search information about
 * a user, a group or other addressable resources.  IMPSSearch
 * results are extensions of
 * {@link javax.imps.IMPSAddress}
 * .
 * A group can be searched for by providing all or portion of the
 * group identifier, name, description, topic, or ownership.
 * <p>
 * A resource can be searched by providing all or part of the
 * resource's name (e.g. user first name or last name), email address
 * or resource identifier of some sort.
 * </p>
 * It is possible to narrow the search down to a specific object type
 * or class.  For instance, only search for users, only search for groups.
 * 
 * 
 * 
 * <pre>
 * // Performs a search operation.
 * String[] attributes = {"URI"};
 * IMPSStatus status = null;
 * IMPSSearchResult searchresult = null;
 * IMPSSearch searchObject;
 * IMPSAddress[] matchedEntries;
 * try {
 *   searchObject = factory.createIMPSSearch(connection);
 * } catch ( IMPSException e) {}
 * 
 * IMPSTid tid = searchObject.search("somedomian.com", "john", attributes, 10);
 * // joinTransaction is application method defined in
 * // section
 * status = joinTransaction(tid);
 * if(status.getResultCode == STATUS.OK){
 *   searchresult = (IMPSSearchResult)status.getData();
 *   //check if the search operation is complete ot it needs to con
 *   while (searchresult.getStatus() != IMPSSearchResult.LIMIT_REACHED) {
 *     matchedEntries = searchResult.getEntries();
 *     for (int i=0; i < matchedEntries.length; i++) {
 *       System.out.println("Matching Entries' IMPSAddress :" + matchedEntries[i].getURI());
 *     }
 *     // continuation search
 *     tid = searchObject.continueSearch(searchResult, 10);
 *     // joinTransaction is application method defined in
 *     // section
 *     status = joinTransaction(tid);
 *     if (status.getResultCode() == IMPSStatus.OK) {
 *       searchResult = (IMPSSearchResult)status.getData();
 *     } else {
 *       // search wasn't successful so break the loop.
 *       break;
 *     }
 *   }
 * }
 * </pre>
 * 
 */
public interface IMPSSearch {
    /**
     * Continue search for a particular item.  This method can be
     * called after a previous search reached the size limit.
     * @param result last received result
     * @param sizeLimit max number of entries to be returned by this
     *  search command.
     * @return an instance of IMPSTid (transaction id) that identifies this transaction.
     */
    javax.imps.IMPSTid continueSearch(javax.imps.IMPSSearchResult result, int sizeLimit);

    /**
     * Start a search.  Results are returned asynchronously as individual
     * messages.
     * @param realm identifies the domain, scope, or realm in which the
     *  search should be performed.  The format of the realm is implementation
     *  specific.  An implementation may support multiple formats.
     * @param pattern all or portion of an attribute value identifying the
     *  resources to retrieve.
     * @param attributes list of attributes which should be provided,
     *  if present, as part of each returned search results.
     * @param sizeLimit max number of entries to be returned by this
     *  search command.
     * @return an instance of IMPSTid (transaction id) that identifies this transaction.
     *  upon successful completion, the data
     *  provided by the status is a search handle of type {@link javax.imps.IMPSSearchResult}.
     */
    javax.imps.IMPSTid search(java.lang.String realm, java.lang.String pattern, java.lang.String[] attributes, int sizeLimit);

    /**
     * Tell the service to stop sending results.
     * This requires that the server provided at least one response.
     * @param result a previously received search result.
     *  provides a handle on the search, used internally
     *  by the API to construct a stop search request.
     * @return an instance of IMPSTid (transaction id) that identifies this transaction.
     *  Upon completion, no data is provided in the
     *  response.
     */
    javax.imps.IMPSTid stopSearch(javax.imps.IMPSSearchResult result);

}

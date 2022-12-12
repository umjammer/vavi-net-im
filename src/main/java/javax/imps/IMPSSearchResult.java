package javax.imps;
/**
 * Represents the content of one message sent from the
 * server as a result of a IMPSSearch.
 * Multiple search result messages can be received during a search.
 * A search result may or may not contain entries.
 */
public interface IMPSSearchResult {
    /**
     * IMPSStatus value when the search failed due to access denied
     */
    int ACCESS_DENIED=3;

    /**
     * IMPSStatus value when the results of the search reach the specified limit
     */
    int LIMIT_REACHED=1;

    /**
     * IMPSStatus value when the search failed due to an unspecified error
     */
    int OTHER_ERROR=99;

    /**
     * IMPSStatus value when a search is active
     */
    int PENDING=-1;

    /**
     * IMPSStatus value when a search is successful
     */
    int SUCCESS=0;

    /**
     * Get the results of the search response
     * @return addresses corresponding to the search request
     */
    javax.imps.IMPSAddress[] getEntries();

    /**
     * Get the status of an search request
     * @return the status of the search request
     */
    int getStatus();

    /**
     * Determine if the search request has completed
     * @return true if the search request has been completed, false otherwise
     */
    boolean isComplete();

}

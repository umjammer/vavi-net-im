package javax.imps;
/**
 * Mechanism by which protocol specific features and data elements
 * can be accessed or manipulated by the application.
 * Interfaces which use this mechanism include:
 * <p>
 * IMPSGroup - augment the baseline group properties
 * InstantMessage - protocol specific IM headers and/or attributes
 * IMPSInvitation.invite - resources associated with the invite
 * PresenceInformation - augment the baseline presence attributes
 * </p>
 * 
 * 
 * 
 * 
 * <pre>
 * // Example of how to manipulate Extensible Object
 * // In particular this example shows how to set different
 * // values for groupProperties
 * Extensible groupProperties;
 * IMPSGroup group;
 * IMPSAddress groupAddress;
 * try {
 *   groupProperties = factory.createExtensible();
 *   group = factory.createIMPSGroup();
 *   groupAddress = factory.createIMPSAddress("wv:owner/movie-group@example.com",
 *   IMPSAddress.ADDRESS_GROUP);
 * } catch (IMPSException e) {
 *   e.printStackTrace();
 * }
 * groupProperties.setText("Name", "MOVIE-GROUP");
 * groupProperties.setText("AccessType", "RESTRCITED");
 * groupProperties.setText("Type", "PRIVATE");
 * groupProperties.setText("PrivateMessaging", "TRUE");
 * groupProperties.setText("Searchable", "TRUE");
 * groupProperties.setText("Topic", "Discuss Oscar Movies");
 * 
 * // screen name same as groupAddress
 * IMPSTid tid = setGroup(groupAddress, groupProperties, groupAddress, true, true);
 * // joinTransaction is application method defined in
 * section
 * IMPSStatus status = joinTransaction(tid);
 * if(status.getResultCode() == IMPSStatus.OK){
 * // group was created successfully. do some other operations
 * }
 * </pre>
 */
public interface Extensible {
    int BINARY_ITEM_TYPE=1;

    int BOOLEAN_ITEM_TYPE=5;

    int INTEGER_ITEM_TYPE=3;

    int STRING_ITEM_TYPE=2;

    int SUBEXTENSIBLE_ITEM_TYPE=4;

    /**
     * Get the binary data item of the name itemName.
     * @param itemName the name of the item whose data are going to be returned.
     * @return The binary data in the item of the name itemName
     */
    byte[] getBinary(java.lang.String itemName);

    /**
     * Get the boolean data item of the name itemName.
     * @param itemName the name of the item whose data are going to be returned.
     * @return The boolean value of the data in the item of the name itemName
     */
    boolean getBoolean(java.lang.String itemName);

    /**
     * Get the integer data item of the name itemName.
     * @param itemName the name of the item whose data are going to be returned.
     * @return The integer value of the data in the item of the name itemName
     */
    int getInt(java.lang.String itemName);

    /**
     * Get a list of the names for all the data items this Extensible reference has.
     * @return The names for the data items in this Extensible reference.
     */
    java.lang.String[] getItemNameList();

    /**
     * Get the type of the data item of the name itemName
     * (binary, text etc). The following are the basic item types:
     * 1. Text (String)
     * 2. Binary (Byte[])
     * 3. Extensible (Extensible)
     * @param itemName the name of the item whose type number is going to be returned.
     *  If the itemName does not exist return value SHALL be 0.
     * @return The type number for the item of the name itemName.
     *  The expected value ranges from 1 through 3.
     */
    int getItemType(java.lang.String itemName);

    /**
     * Gets the Extensible data item of the name itemName.
     * @param itemName the name of the item whose data are going to be returned.
     * @return The data in the item of the name itemName
     */
    javax.imps.Extensible getSubExtensible(java.lang.String itemName);

    /**
     * Get the text data item of the name itemName.
     * @param itemName the name of the item whose data are going to be returned.
     * @return The text data in the item of the name itemName
     */
    java.lang.String[] getText(java.lang.String itemName);

    /**
     * Check whether this Extensible reference has a data item of the name itemName.
     * @param itemName the name of the item whose existence is going to be checked.
     * @return true if an item of the name itemName exists, otherwise
     *  false.
     */
    boolean hasItem(java.lang.String itemName);

    /**
     * Set the binary data item of the name itemName.
     * @param itemName the name of the item whose data are going to be set.
     * @param value the new data for the item of the name itemName.
     */
    void setBinary(java.lang.String itemName, byte[] value);

    /**
     * Set the boolean data item of the name itemName.
     * @param itemName the name of the item whose data are going to be set.
     * @param value the new data for the item of the name itemName.
     */
    void setBoolean(java.lang.String itemName, boolean value);

    /**
     * Set the integer data item of the name itemName.
     * @param itemName the name of the item whose data are going to be set.
     * @param value the new data for the item of the name itemName.
     */
    void setInt(java.lang.String itemName, int value);

    /**
     * Set the  data item of the name itemName.
     * @param itemName the name of the item whose data are going to be set.
     * @param value the new data for the item of the name itemName.
     */
    void setSubExtensible(java.lang.String itemName, javax.imps.Extensible value);

    /**
     * Set the text data item of the name itemName.
     * @param itemName the name of the item whose data are going to be set.
     * @param value the new data for the item of the name itemName.
     */
    void setText(java.lang.String itemName, java.lang.String value);

}

package javax.imps;
/**
 * Represents negotiated service features and capabilities.
 * 
 * 
 * <pre>
 * // IMPSService example. Shows how to negotiate IMPSServiceFeatures information
 * 
 * IMPSServiceFeatures servicefeatures = factory.createIMPSServiceFeature();
 * 
 * servicefeatures.setFeature(IMPSServiceFeatures.FEATURE_IM_SEND);
 * servicefeatures.setFeature(IMPSServiceFeatures.FEATURE_IM_RECV);
 * servicefeatures.setFeature(IMPSServiceFeatures.FEATURE_IM_ACL);
 * servicefeatures.setFeature(IMPSServiceFeatures.FEATURE_GROUP_MGT);
 * servicefeatures.setFeature(IMPSServiceFeatures.FEATURE_GROUP_ACL);
 * servicefeatures.setFeature(IMPSServiceFeatures.FEATURE_GROUP_INVITE);
 * servicefeatures.setFeature(IMPSServiceFeatures.FEATURE_INVITATION);
 * servicefeatures.setFeature(IMPSServiceFeatures.FEATURE_SEARCH);
 * servicefeatures.setFeature(IMPSServiceFeatures.FEATURE_SEARCH_STOP);
 * servicefeatures.setFeature(IMPSServiceFeatures.FEATURE_SEARCH_PAGE);
 * servicefeatures.setFeature(IMPSServiceFeatures.FEATURE_VERIFYID);
 * servicefeatures.setFeature(IMPSServiceFeatures.FEATURE_CONTACTLIST);
 * servicefeatures.setFeature(IMPSServiceFeatures.FEATURE_PUBLISHER);
 * servicefeatures.setFeature(IMPSServiceFeatures.FEATURE_PUBLISHER_ACL);
 * servicefeatures.setFeature(IMPSServiceFeatures.FEATURE_WATCHER);
 * 
 * IMPSTid tid = connection.negotiateService(serviceFeatures);
 * // joinTransaction is application method defined in
 * // section
 * IMPSStatus status = joinTransaction(tid);
 * if(status.getResultCode() == STATUS.OK) {
 *   IMPSServiceFeatures negotiatedServiceFeatures = (IMPSServiceFeatures)status.getData();
 *   String [] features = negotiatedServiceFeatures.getFeatures();
 *   for (int i = 0; i < features.length; i++){
 *     // prints out all the negotiated feature names.
 *     System.out.println(features[i]);
 *   }
 * </pre>
 * 
 */
public interface IMPSServiceFeatures {
    java.lang.String FEATURE_CONTACTLIST="CONTACTLIST";

    java.lang.String FEATURE_GROUP="GROUP";

    java.lang.String FEATURE_GROUP_ACL="GROUP_ACL";

    java.lang.String FEATURE_GROUP_INVITE="GROUP_INVITE";

    java.lang.String FEATURE_GROUP_MGT="GROUP_MGT";

    java.lang.String FEATURE_IM_ACL="IM_ACL";

    java.lang.String FEATURE_IM_RECV="IM_RECV";

    java.lang.String FEATURE_IM_SEND="IM_SEND";

    java.lang.String FEATURE_INVITATION="INVITATION";

    java.lang.String FEATURE_PUBLISHER="PUBLISHER";

    java.lang.String FEATURE_PUBLISHER_ACL="PUBLISHER_ACL";

    java.lang.String FEATURE_SEARCH="SEARCH";

    java.lang.String FEATURE_SEARCH_PAGE="SEARCH_PAGE";

    java.lang.String FEATURE_SEARCH_STOP="SEARCH_STOP";

    java.lang.String FEATURE_WATCHER="WATCHER";

    /**
     * IM delivery method definition - Notify-Get
     */
    int NOTIFY_GET=1;

    /**
     * IM delivery method definition - Push
     */
    int PUSH=0;

    /**
     * Get the default content encoding
     * @return the default content encoding
     */
    java.lang.String getDefaultEncoding();

    /**
     * Get the default language setting
     * @return the default language
     */
    java.lang.String getDefaultLanguage();

    /**
     * Get the delivery method for an IM, either PUSH or NOTIFY-GET
     * @return the delivery method
     */
    int getDeliveryMethod();

    /**
     * Get the default feature set
     * @return the default feature set
     */
    java.lang.String[] getFeatures();

    /**
     * Get the maximum content length in an IM
     * @return the maximum length of content of an IM, in bytes
     */
    int getMaxContentLength();

    /**
     * Remove the feature from the feature set
     * @param feature feature to be removed
     */
    void removeFeature(java.lang.String feature);

    /**
     * Set the deliver method for an IM, either PUSH or NOTIFY-GET
     * @param deliveryMethod either PUSH or NOTIFY-GET
     */
    void setDeliveryMethod(int deliveryMethod);

    /**
     * Set a feature in the feature set
     * @param feature the feature
     */
    void setFeature(java.lang.String feature);

}

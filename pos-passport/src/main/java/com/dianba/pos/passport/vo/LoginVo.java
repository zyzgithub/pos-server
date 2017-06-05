package com.dianba.pos.passport.vo;

/**
 * Created by zhangyong on 2017/6/5.
 */
public class LoginVo {


    /**
     * code : 0
     * msg : 请求成功
     * response : {"realName":"","showName":"zyz1324919","phoneNumber":"13249196272","roleValue":"11","showPhoneNumber":"132****6272","headImage":"","versionMessage":{"upgradeType":1,"title":"已是最新版本","showVersion":"V1.0"},"accessToken":"4101fe97a5ea97ca7c0786d881fc3266","passportId":100045}
     */




        /**
         * realName :
         * showName : zyz1324919
         * phoneNumber : 13249196272
         * roleValue : 11
         * showPhoneNumber : 132****6272
         * headImage :
         * versionMessage : {"upgradeType":1,"title":"已是最新版本","showVersion":"V1.0"}
         * accessToken : 4101fe97a5ea97ca7c0786d881fc3266
         * passportId : 100045
         */

        private String realName;
        private String showName;
        private String phoneNumber;
        private String roleValue;
        private String showPhoneNumber;
        private String headImage;
        private VersionMessageBean versionMessage;
        private String accessToken;
        private int passportId;

        private int accountType;

        private String accountTypeName;
        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getShowName() {
            return showName;
        }

        public void setShowName(String showName) {
            this.showName = showName;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getRoleValue() {
            return roleValue;
        }

        public void setRoleValue(String roleValue) {
            this.roleValue = roleValue;
        }

        public String getShowPhoneNumber() {
            return showPhoneNumber;
        }

        public void setShowPhoneNumber(String showPhoneNumber) {
            this.showPhoneNumber = showPhoneNumber;
        }

        public String getHeadImage() {
            return headImage;
        }

        public void setHeadImage(String headImage) {
            this.headImage = headImage;
        }

        public VersionMessageBean getVersionMessage() {
            return versionMessage;
        }

        public void setVersionMessage(VersionMessageBean versionMessage) {
            this.versionMessage = versionMessage;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public int getPassportId() {
            return passportId;
        }

        public void setPassportId(int passportId) {
            this.passportId = passportId;
        }


        public int getAccountType() {
            return accountType;
        }

        public void setAccountType(int accountType) {
            this.accountType = accountType;
        }

        public String getAccountTypeName() {
            return accountTypeName;
        }

        public void setAccountTypeName(String accountTypeName) {
            this.accountTypeName = accountTypeName;
        }

        public static class VersionMessageBean {
            /**
             * upgradeType : 1
             * title : 已是最新版本
             * showVersion : V1.0
             */

            private int upgradeType;
            private String title;
            private String showVersion;

            public int getUpgradeType() {
                return upgradeType;
            }

            public void setUpgradeType(int upgradeType) {
                this.upgradeType = upgradeType;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getShowVersion() {
                return showVersion;
            }

            public void setShowVersion(String showVersion) {
                this.showVersion = showVersion;
            }
        }

}

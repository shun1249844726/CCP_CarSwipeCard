package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean;

/**
 * Created by xushun on 2017/11/8.
 * 功能描述：
 * 心情：
 */

public class AppVersionEntity {

    /**
     * cmd_type : app_version
     * cmd_content : {"version_name":"1.0.3","version_code":"3","device_iemi":"xushun"}
     */

    private String cmd_type;
    private CmdContentBean cmd_content;

    public String getCmd_type() {
        return cmd_type;
    }

    public void setCmd_type(String cmd_type) {
        this.cmd_type = cmd_type;
    }

    public CmdContentBean getCmd_content() {
        return cmd_content;
    }

    public void setCmd_content(CmdContentBean cmd_content) {
        this.cmd_content = cmd_content;
    }

    public static class CmdContentBean {
        /**
         * version_name : 1.0.3
         * version_code : 3
         * device_iemi : xushun
         */

        private String version_name;
        private String version_code;
        private String device_iemi;

        public String getVersion_name() {
            return version_name;
        }

        public void setVersion_name(String version_name) {
            this.version_name = version_name;
        }

        public String getVersion_code() {
            return version_code;
        }

        public void setVersion_code(String version_code) {
            this.version_code = version_code;
        }

        public String getDevice_iemi() {
            return device_iemi;
        }

        public void setDevice_iemi(String device_iemi) {
            this.device_iemi = device_iemi;
        }
    }
}

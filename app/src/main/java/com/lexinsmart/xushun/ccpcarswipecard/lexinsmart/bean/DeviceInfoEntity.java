package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean;

/**
 * Created by xushun on 2017/11/22.
 * 功能描述：
 * 心情：
 */

public class DeviceInfoEntity {


    /**
     * cmd_type : device_info
     * cmd_content : {"device_iemi":"861691036632481","time":"2017-10-29 13:27:00.00"}
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
         * device_iemi : 861691036632481
         * time : 2017-10-29 13:27:00.00
         */

        private String device_iemi;
        private String time;

        public String getDevice_iemi() {
            return device_iemi;
        }

        public void setDevice_iemi(String device_iemi) {
            this.device_iemi = device_iemi;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}

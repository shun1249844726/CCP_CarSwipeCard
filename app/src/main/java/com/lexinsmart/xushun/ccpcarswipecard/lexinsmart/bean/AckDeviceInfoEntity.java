package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean;

/**
 * Created by xushun on 2017/11/22.
 * 功能描述：
 * 心情：
 */

public class AckDeviceInfoEntity {

    /**
     * cmd_type : ack_device_info
     * status_code : 0
     * cmd_content : {"device_iemi":"861691036632481","device_number":null,"carno":null,"driver_name":null,"remarks":null}
     */

    private String cmd_type;
    private int status_code;
    private CmdContentBean cmd_content;

    public String getCmd_type() {
        return cmd_type;
    }

    public void setCmd_type(String cmd_type) {
        this.cmd_type = cmd_type;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
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
         * device_number : null
         * carno : null
         * driver_name : null
         * remarks : null
         */

        private String device_iemi;
        private Object device_number;
        private Object carno;
        private Object driver_name;
        private Object remarks;

        public String getDevice_iemi() {
            return device_iemi;
        }

        public void setDevice_iemi(String device_iemi) {
            this.device_iemi = device_iemi;
        }

        public Object getDevice_number() {
            return device_number;
        }

        public void setDevice_number(Object device_number) {
            this.device_number = device_number;
        }

        public Object getCarno() {
            return carno;
        }

        public void setCarno(Object carno) {
            this.carno = carno;
        }

        public Object getDriver_name() {
            return driver_name;
        }

        public void setDriver_name(Object driver_name) {
            this.driver_name = driver_name;
        }

        public Object getRemarks() {
            return remarks;
        }

        public void setRemarks(Object remarks) {
            this.remarks = remarks;
        }
    }
}

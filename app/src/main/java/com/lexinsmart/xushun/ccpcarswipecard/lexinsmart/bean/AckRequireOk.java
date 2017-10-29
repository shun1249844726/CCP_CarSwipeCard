package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean;

/**
 * Created by xushun on 2017/10/29.
 * 功能描述：
 * 心情：
 */

public class AckRequireOk {


    /**
     * cmd_type : ack_require
     * status_code : 0
     * content : {"name":"xushun","unit":"C1011","card_number":"2198989898"}
     */

    private String cmd_type;
    private int status_code;
    private ContentBean content;

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

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * name : xushun
         * unit : C1011
         * card_number : 2198989898
         */

        private String name;
        private String staff_number;
        private String card_number;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStaff_number() {
            return staff_number;
        }

        public void setStaff_number(String staff_number) {
            this.staff_number = staff_number;
        }

        public String getCard_number() {
            return card_number;
        }

        public void setCard_number(String card_number) {
            this.card_number = card_number;
        }
    }
}

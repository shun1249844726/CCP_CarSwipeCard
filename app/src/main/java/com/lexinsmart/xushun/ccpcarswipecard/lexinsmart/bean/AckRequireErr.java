package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean;

/**
 * Created by xushun on 2017/10/29.
 * 功能描述：
 * 心情：
 */

public class AckRequireErr {

    /**
     * cmd_type : ack_require
     * status_code : 1
     * content : {"card_number":"21989898","msg":"hahahaha"}
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
         * card_number : 21989898
         * msg : hahahaha
         */

        private String card_number;
        private String msg;

        public String getCard_number() {
            return card_number;
        }

        public void setCard_number(String card_number) {
            this.card_number = card_number;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}

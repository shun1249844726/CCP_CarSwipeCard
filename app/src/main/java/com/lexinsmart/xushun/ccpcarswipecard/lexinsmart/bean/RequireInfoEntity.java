package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean;

/**
 * Created by xushun on 2017/10/29.
 * 功能描述：
 * 心情：
 */

public class RequireInfoEntity {

    /**
     * cmd_type : require_info
     * content : {"card_number":"2198898989"}
     */

    private String cmd_type;
    private ContentBean content;

    public String getCmd_type() {
        return cmd_type;
    }

    public void setCmd_type(String cmd_type) {
        this.cmd_type = cmd_type;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * card_number : 2198898989
         */

        private String card_number;

        public String getCard_number() {
            return card_number;
        }

        public void setCard_number(String card_number) {
            this.card_number = card_number;
        }
    }
}

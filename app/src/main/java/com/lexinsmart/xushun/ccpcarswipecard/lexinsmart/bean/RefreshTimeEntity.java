package com.lexinsmart.xushun.ccpcarswipecard.lexinsmart.bean;

/**
 * Created by xushun on 2017/10/29.
 * 功能描述：
 * 心情：
 */

public class RefreshTimeEntity {

    /**
     * cmd_type : refresh_time
     * content : {"card_number":"21989898","direction":1,"time":"2017-10-29 13:27:00"}
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
         * card_number : 21989898
         * direction : 1
         * time : 2017-10-29 13:27:00
         */

        private String card_number;
        private int direction;
        private String time;

        public String getCard_number() {
            return card_number;
        }

        public void setCard_number(String card_number) {
            this.card_number = card_number;
        }

        public int getDirection() {
            return direction;
        }

        public void setDirection(int direction) {
            this.direction = direction;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}

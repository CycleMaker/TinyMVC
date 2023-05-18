package org.tiny.mvc.core.banner;

/**
 * @author: wuzihan (wuzihan@youzan.com)
 * @create: 2023-05-18 12 :06
 * @description
 */
public class DefaultBannerPrinter implements MVCBannerPrinter {
    @Override
    public void printBanner() {
        System.out.println(
                " _____ _            ___  ____   _ _____ \n" +
                        "|_   _(_)           |  \\/  | | | /  __ \\\n" +
                        "  | |  _ _ __  _   _| .  . | | | | /  \\/\n" +
                        "  | | | | '_ \\| | | | |\\/| | | | | |    \n" +
                        "  | | | | | | | |_| | |  | \\ \\_/ | \\__/\\\n" +
                        "  \\_/ |_|_| |_|\\__, \\_|  |_/\\___/ \\____/\n" +
                        "                __/ |                   \n" +
                        "               |___/                   "
        );
    }
}

package top.kanetah.planH.service;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CheckMobileService {

    private String phoneReg = "\\b(ip(hone|od)|android|opera m(ob|in)i"
            + "|windows (phone|ce)|blackberry"
            + "|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp"
            + "|laystation portable)|nokia|fennec|htc[-_]"
            + "|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
    private String tableReg = "\\b(ipad|tablet|(Nexus 7)|up.browser"
            + "|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";

    //移动设备正则匹配：手机端、平板
    private Pattern phonePat = Pattern.compile(phoneReg, Pattern.CASE_INSENSITIVE);
    private Pattern tablePat = Pattern.compile(tableReg, Pattern.CASE_INSENSITIVE);

    /**
     * 检测是否是移动设备访问
     *
     * @param userAgent 浏览器标识
     * @return true:移动设备接入，false:pc端接入
     */
    private boolean check(String userAgent) {
        if (null == userAgent)
            userAgent = "";
        // 匹配
        Matcher matcherPhone = phonePat.matcher(userAgent);
        Matcher matcherTable = tablePat.matcher(userAgent);
        return matcherPhone.find() || matcherTable.find();
    }

    public boolean check(HttpServletRequest request) throws IOException {

        boolean isFromMobile;
        HttpSession session = request.getSession();
        //检查是否已经记录访问方式（移动端或pc端）
        if (session.getAttribute("ua") != null)
            isFromMobile = session.getAttribute("ua").equals("mobile");
        else try {
            //获取ua，用来判断是否为移动端访问
            String userAgent = request.getHeader("USER-AGENT").toLowerCase();
            isFromMobile = check(userAgent);
            //判断是否为移动端访问
            if (isFromMobile)
                session.setAttribute("ua", "mobile");
            else
                session.setAttribute("ua", "pc");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return isFromMobile;
    }
}

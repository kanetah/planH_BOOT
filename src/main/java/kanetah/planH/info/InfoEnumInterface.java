package kanetah.planH.info;

/**
 * created by kane on 2017/08/11
 *
 * 信息报表枚举类接口
 */
public interface InfoEnumInterface {

    /**
     * 执行信息类的一个方法
     * @param object 被执行的信息类对象
     * @return 返回值
     */
    Object invokeMargetMethod(Object object);

    /**
     * 获取枚举值
     *
     * @return 枚举值
     */
    String getValue();
}

package top.kanetah.planH.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by kane on 2017/08/23
 *
 * 向前端提供信息的接口
 */
public interface Info {

    // 前端信息实体类类名列表（非完全限定名）
    List<String> infoClassNameList = new ArrayList<>();
    // 前端信息实体类完全限定名及其Class对象哈希图
    Map<String, Class<?>> classMap = new HashMap<>();
    // 前端信息实体类完全限定名及其来源实体类特征值哈希图
    Map<String, String> classTokenMap = new HashMap<>();

    /**
     * 获取动态生成的前端信息实体类Class对象
     *
     * @param name 需要类的完全限定名
     * @return Class对象
     */
    static Class<?> forName(String name) {
        return classMap.get(name);
    }

    /**
     * 通过一个或多个源实体对象获得前端信息实体对象
     *
     * @param args 源实体对象
     * @return 前端信息实体对象
     */
    Object byOrigin(Object... args);

    /**
     * 通过前端信息实体类Class对象获取用于操作该类的动态枚举类
     * 并将该枚举类以编译前定义的接口的形式返回
     *
     * @param targetClass 前端信息实体类Class对象
     * @return 操作该类前端信息实体的动态枚举所实现的接口
     */
    List<InfoEnumInterface> getEnumList(Class targetClass);
}

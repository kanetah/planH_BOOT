package top.kanetah.planH.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import top.kanetah.planH.entity.relationship.SubordinateUser;
import top.kanetah.planH.entity.node.*;
import top.kanetah.planH.entity.relationship.Authority;
import top.kanetah.planH.entity.relationship.SubordinateTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.kanetah.planH.info.Info;
import top.kanetah.planH.tools.FileTool;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

@Service
public class AdminService implements InitializingBean {

    private final Info info;
    private final RepositoryService repositoryService;
    @Value(value = "${kanetah.planH.admin.password}")
    private String adminPassword;
    @Value(value = "${kanetah.planH.admin.poi.config}")
    private Resource poiConfigResource;

    private String userCodePrefix;
    private String userCodeMark;
    private String userNameMark;

    @Autowired
    public AdminService(
            Info info,
            RepositoryService repositoryService
    ) {
        this.info = info;
        this.repositoryService = repositoryService;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void afterPropertiesSet() throws Exception {
        Class<? extends AdminService> clazz = AdminService.class;
        new ObjectMapper().readValue(
                FileTool.inputStreamToFile(
                        poiConfigResource.getInputStream()
                ),
                Map.class
        ).forEach((k, v) -> {
            try {
                Field field = clazz.getDeclaredField(k.toString());
                field.setAccessible(true);
                field.set(AdminService.this, v);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void createTask(Task task) {
        TaskRoot taskRoot = repositoryService.taskRootRepository.find();
        SubordinateTask subordinateTask = new SubordinateTask(taskRoot, task);
        repositoryService.subordinateTaskRepository.save(subordinateTask);
    }

    public void updateTask(
            Long id,
            String subject,
            String title,
            String content,
            String type,
            String format,
            String deadline
    ) {
        Optional<Task> optional = repositoryService.taskRepository.findById(id);
        assert optional.isPresent();
        Task task = optional.get();
        task.setSubject(subject);
        task.setTitle(title);
        task.setContent(content);
        task.setFileFormat(type);
        task.setSaveFormat(format);
        task.setDeadline(deadline);
        repositoryService.taskRepository.save(task);
    }

    public void createUser(User user) {
        UserRoot userRoot = repositoryService.userRootRepository.find();
        SubordinateUser subordinateUser = new SubordinateUser(userRoot, user);
        repositoryService.subordinateUserRepository.save(subordinateUser);

        Role role = repositoryService.roleRepository.findUserRole();
        user = repositoryService.userRepository.findByUserName(user.getUserName());
        Authority authority = new Authority(user, role);
        repositoryService.authorityRepository.save(authority);
    }

    public void resetAdmin() {
        User admin = new User(Long.valueOf(adminPassword), "admin");
        UserRoot userRoot = repositoryService.userRootRepository.find();
        SubordinateUser subordinateUser = new SubordinateUser(userRoot, admin);
        repositoryService.subordinateUserRepository.save(subordinateUser);

        Role role = repositoryService.roleRepository.findAdminRole();
        admin = repositoryService.userRepository.findByUserName(admin.getUserName());
        Authority authority = new Authority(admin, role);
        repositoryService.authorityRepository.save(authority);
    }

    public void batchCreateUser(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        POIFSFileSystem fs = new POIFSFileSystem(inputStream);
        HSSFWorkbook workbook = new HSSFWorkbook(fs);
        HSSFSheet sheet = workbook.getSheetAt(0);
        HSSFRow row = sheet.getRow(1);
        final Map<String, Integer> index = new HashMap<>();
        row.forEach(r -> {
            if (r.getStringCellValue().equals(userCodeMark))
                index.put(userCodeMark, r.getColumnIndex());
            if (r.getStringCellValue().equals(userNameMark))
                index.put(userNameMark, r.getColumnIndex());
        });

        repositoryService.userRepository.deleteAll();
        resetAdmin();

        for (int i = 2; i <= sheet.getLastRowNum(); ++i) {
            row = sheet.getRow(i);
            createUser(new User(
                    Long.valueOf(
                            row.getCell(index.get(userCodeMark)).getStringCellValue()
                                    .substring(userCodePrefix.length())
                    ),
                    row.getCell(index.get(userNameMark)).getStringCellValue()
            ));
        }
    }

    public String getUserCodePrefix() {
        return userCodePrefix;
    }

    public List<Object> getAllTask() {
        List<Object> tasks = new ArrayList<>();
        repositoryService.taskRepository.findAll().forEach(task ->
                tasks.add(info.byOrigin(task)));
        return tasks;
    }
}

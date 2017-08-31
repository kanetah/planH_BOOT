package top.kanetah.planH.service;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import top.kanetah.planH.entity.relationship.SubordinateUser;
import top.kanetah.planH.entity.node.*;
import top.kanetah.planH.entity.relationship.Authority;
import top.kanetah.planH.entity.relationship.SubordinateTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdminService {

    private final RepositoryService repositoryService;
    @Value(value = "${kanetah.planH.userCodePrefix}")
    private String codePrefix;
    @Value(value = "${kanetah.planH.userCodeMark}")
    private String codeMark;
    @Value(value = "${kanetah.planH.userNameMark}")
    private String nameMark;

    @Autowired
    public AdminService(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    public void createTask(Task task) {

        TaskRoot taskRoot = repositoryService.taskRootRepository.find();
        SubordinateTask subordinateTask = new SubordinateTask(taskRoot, task);
        repositoryService.subordinateTaskRepository.save(subordinateTask);
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

        User admin = new User(0, "admin");
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
            if (r.getStringCellValue().equals(codeMark))
                index.put(codeMark, r.getColumnIndex());
            if (r.getStringCellValue().equals(nameMark))
                index.put(nameMark, r.getColumnIndex());
        });

        repositoryService.userRepository.deleteAll();
        resetAdmin();

        for (int i = 2; i <= sheet.getLastRowNum(); ++i) {
            row = sheet.getRow(i);
            String code = row.getCell(index.get(codeMark)).getStringCellValue()
                    .substring(codePrefix.length());
            String name = row.getCell(index.get(nameMark)).getStringCellValue();
            createUser(new User(Long.valueOf(code), name));
        }
    }
}

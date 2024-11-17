package vn.id.vuductrieu.tlcn_be.service.MongoService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.EmployeeCollection;
import vn.id.vuductrieu.tlcn_be.repository.mongodb.EmployeeRepo;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeMongoService {

    private final EmployeeRepo employeeRepo;
    public EmployeeCollection login(EmployeeCollection employee) {
        EmployeeCollection employeeCollection = employeeRepo.findByAccount(employee.getAccount());

        if (employeeCollection == null) {
            throw new IllegalArgumentException("Account not found");
        }

        if (!BCrypt.checkpw(employee.getPassword(), employeeCollection.getPassword())) {
            throw new IllegalArgumentException("Password is incorrect");
        }

        return employeeCollection;
    }

    public List<EmployeeCollection> getAll() {
        try {
            return employeeRepo.findAll().stream().map(employee -> {
                employee.setPassword(null);
                return employee;
            }).toList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}

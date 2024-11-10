package vn.id.vuductrieu.tlcn_be.service.MongoService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.EmployeeCollection;
import vn.id.vuductrieu.tlcn_be.repository.mongodb.EmployeeRepo;

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
}

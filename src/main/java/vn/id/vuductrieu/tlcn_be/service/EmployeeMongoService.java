package vn.id.vuductrieu.tlcn_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import vn.id.vuductrieu.tlcn_be.entity.EmployeeCollection;
import vn.id.vuductrieu.tlcn_be.repository.EmployeeRepo;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeMongoService {

    private final EmployeeRepo employeeRepo;
    public EmployeeCollection login(EmployeeCollection employee) {
        EmployeeCollection employeeCollection = employeeRepo.findByAccount(employee.getAccount());

        if (employeeCollection == null) {
            throw new IllegalArgumentException("Không tìm thấy tài khoản");
        }

        if (!BCrypt.checkpw(employee.getPassword(), employeeCollection.getPassword())) {
            throw new IllegalArgumentException("Sai mật khẩu");
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

    public void create(EmployeeCollection employee) {
        try {
            EmployeeCollection employeeCollection = employeeRepo.findByAccount(employee.getAccount());
            if (employeeCollection != null) {
                throw new IllegalArgumentException("Tài khoản đã tồn tại");
            }
            employee.setPassword(BCrypt.hashpw(employee.getPassword(), BCrypt.gensalt()));
            employeeRepo.insert(employee);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void update(EmployeeCollection employee) {
        try {
            EmployeeCollection employeeCollection = employeeRepo.findById(employee.getId()).orElse(null);
            if (employeeCollection == null) {
                throw new IllegalArgumentException("Không tìm thấy nhân viên");
            }
            // if field in employee is null, keep the old value
            if (employee.getName() != null) {
                employeeCollection.setName(employee.getName());
            }
            if (employee.getEmail() != null) {
                employeeCollection.setEmail(employee.getEmail());
            }
            if (employee.getPhone() != null) {
                employeeCollection.setPhone(employee.getPhone());
            }
            if (employee.getAddress() != null) {
                employeeCollection.setAddress(employee.getAddress());
            }
            if (employee.getAccount() != null) {
                employeeCollection.setAccount(employee.getAccount());
            }
            if (employee.getPassword() != null) {
                employeeCollection.setPassword(BCrypt.hashpw(employee.getPassword(), BCrypt.gensalt()));
            }
            if (employee.getRole() != null) {
                employeeCollection.setRole(employee.getRole());
            }
            if (employee.getStatus() != null) {
                employeeCollection.setStatus(employee.getStatus());
            }
            employeeRepo.save(employeeCollection);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public EmployeeCollection getDetail(String id) {
        try {
            EmployeeCollection employeeCollection = employeeRepo.findById(id).orElse(null);
            if (employeeCollection == null) {
                throw new IllegalArgumentException("Không tìm thấy nhân viên");
            }
            employeeCollection.setPassword(null);
            return employeeCollection;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}

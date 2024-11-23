package vn.id.vuductrieu.tlcn_be.controller.mongdb;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.id.vuductrieu.tlcn_be.constants.Constants;
import vn.id.vuductrieu.tlcn_be.entity.mongodb.EmployeeCollection;
import vn.id.vuductrieu.tlcn_be.service.MongoService.EmployeeMongoService;
import vn.id.vuductrieu.tlcn_be.service.PermissionService;
import vn.id.vuductrieu.tlcn_be.utils.TokenUtils;

import java.util.Map;

@RestController
@RequestMapping("/api/mongo/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeMongoService employeeMongoService;

    private final TokenUtils tokenUtils;

    private final PermissionService permissionService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody EmployeeCollection employee) {
        try {
            EmployeeCollection employeeCollection = employeeMongoService.login(employee);
            Map<String, Object> response = Map.of(
                "id", employeeCollection.getId(),
                "email", employeeCollection.getEmail(),
                "name", employeeCollection.getName(),
                "token", tokenUtils.generateMongoToken(employeeCollection)
            );
            return ResponseEntity.ok().body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            if (!permissionService.checkRole(Constants.Role.ADMIN.getValue())) {
                return ResponseEntity.badRequest().body("Permission denied");
            }
            return ResponseEntity.ok().body(employeeMongoService.getAll());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody EmployeeCollection employee) {
        try {
            if (!permissionService.checkRole(Constants.Role.ADMIN.getValue())) {
                return ResponseEntity.badRequest().body("Permission denied");
            }
            employeeMongoService.create(employee);
            return ResponseEntity.ok().body("Create success");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody EmployeeCollection employee) {
        try {
            if (!permissionService.checkRole(Constants.Role.ADMIN.getValue()) || !permissionService.getUserId().equals(employee.getId())) {
                return ResponseEntity.badRequest().body("Permission denied");
            }
            employeeMongoService.update(employee);
            return ResponseEntity.ok().body("Update success");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getDetail(@PathVariable String id) {
        try {
            String idToken = permissionService.getUserId();
            if (!idToken.equals(id)) {
                return ResponseEntity.badRequest().body("Không có quyền truy cập");
            }
            return ResponseEntity.ok().body(employeeMongoService.getDetail(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}

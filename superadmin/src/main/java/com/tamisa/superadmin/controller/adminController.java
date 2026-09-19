 package com.tamisa.superadmin.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.tamisa.superadmin.Entity.Partner;
import com.tamisa.superadmin.Entity.PartnerCustomer;
import com.tamisa.superadmin.Service.ItadminService;
import com.tamisa.superadmin.dto.Filterdto;

@RestController
@RequestMapping("/tblchannel-partners-admin")
public class adminController {
        @Autowired
        private ItadminService itadminService;

        @PostMapping("/approveOrRejectPartner/{id}")

        public ResponseEntity<?> approveOrRejectPartner(
                        @RequestParam int id,
                        @RequestParam String status,
                        @RequestParam(required = false) String tierName,
                        @RequestParam(required = false) String RejectionReason) {
                Map<String, Object> res = new HashMap<>();

                Partner response = itadminService.approveOrRejectPartner(id, status,
                                tierName, RejectionReason);

                res.put("status", true);

                res.put("data", response);
                res.put("message", "send status");

                return new ResponseEntity<>(res, HttpStatus.OK);
        }
        @GetMapping("/getPartnerById/{id}")
        public ResponseEntity<?> getPartnerById(@PathVariable int id) {
                Map<String, Object> res = new HashMap<>();
                Partner response = itadminService.getPartnerById(id);
                res.put("status", true);

                res.put("data", response);
                res.put("message", "partner fetched successfully");
                return new ResponseEntity<>(res, HttpStatus.OK);
        }

        @DeleteMapping("/deletePartnerById/{id}")
        public ResponseEntity<?> deletePartnerById(@PathVariable int id) {

                Map<String, Object> res = new HashMap<>();

                itadminService.deleteById(id);

                res.put("status", true);
                res.put("message", "Partner deleted successfully");

                return new ResponseEntity<>(res, HttpStatus.OK);
        }

        @DeleteMapping("/deleteAllPartners")
        public ResponseEntity<?> deleteAllPartners() {
                Map<String, Object> res = new HashMap<>();
                itadminService.deleteAll();
                res.put("status", true);
                res.put("message", "Partner deleted successfully");

                return new ResponseEntity<>(res, HttpStatus.OK);
        }

        @PostMapping("/reviewApplication/{id}")
        public ResponseEntity<?> reviewApplication(
                        @PathVariable int id) {

                Map<String, Object> res = new HashMap<>();

                Partner response = itadminService.reviewApplication(id);

                res.put("status", true);

                res.put("data", response);

                res.put("message", "Application is reviewed");

                return new ResponseEntity<>(
                                res,
                                HttpStatus.OK);
        }

        @PostMapping("/createCustomer/{partnerCode}")
        public ResponseEntity<?> createCustomer(
                        @PathVariable String partnerCode,
                        @RequestBody PartnerCustomer customer) {

        PartnerCustomer savedCustomer = itadminService.createCustomer(partnerCode, customer);

                Map<String, Object> response = new HashMap<>();
                response.put("message", "Customer created successfully");
                response.put("data", savedCustomer);

                return ResponseEntity.ok(response);
        }

        @GetMapping("/{partnerCode}")
        public ResponseEntity<Map<String, Object>> getCustomersByPartnerCode(
                        @PathVariable String partnerCode) {

                List<PartnerCustomer> customers = itadminService.getCustomersByPartnerCode(partnerCode);

                Map<String, Object> response = new HashMap<>();
                response.put("message", "Customers fetched successfully");
                response.put("data", customers);
                return ResponseEntity.ok(response);
        }
        
}

package com.project.employeeManager.controller;

import com.project.employeeManager.dto.EmployeeDTO;
import com.project.employeeManager.dto.TaxDeductionDTO;
import com.project.employeeManager.entity.Employee;
import com.project.employeeManager.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;

@RestController
@RequestMapping(value = "/api/employee")
@Validated
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;

	@PostMapping("/v1")
	public ResponseEntity<Employee> createEmployee(@RequestBody @Valid EmployeeDTO employeeDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		Employee employee = employeeService.createEmployee(employeeDTO);
		return new ResponseEntity<>(employee, HttpStatus.CREATED);
	}

	@GetMapping("/{employeeId}/tax-deductions/v1")
	public ResponseEntity<TaxDeductionDTO> getTaxDeduction(@PathVariable String employeeId) throws ParseException {
		TaxDeductionDTO taxDeductionDTO = employeeService.getTaxDeduction(employeeId);
		return new ResponseEntity<>(taxDeductionDTO, HttpStatus.OK);
	}
}

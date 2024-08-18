package com.project.employeeManager.service;

import com.project.employeeManager.dto.EmployeeDTO;
import com.project.employeeManager.dto.TaxDeductionDTO;
import com.project.employeeManager.entity.Employee;
import com.project.employeeManager.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
public class EmployeeService {

	EmployeeRepository employeeRepository;

	public EmployeeService(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	Logger logger = LoggerFactory.getLogger(EmployeeService.class);

	public Employee createEmployee(EmployeeDTO employeeDTO) {

		Employee employee = new Employee();
		employee.setEmployeeId(employeeDTO.getEmployeeId());
		employee.setFirstName(employeeDTO.getFirstName());
		employee.setLastName(employeeDTO.getLastName());
		employee.setEmail(employeeDTO.getEmail());
		if (employeeDTO.getPhoneNumbers().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone numbers list cannot be empty");
		} else {
			for (String number : employeeDTO.getPhoneNumbers()) {
				if (!isNumber(number) || String.valueOf(Long.parseLong(number)).length() != 10) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid phone number " + number);
				}
			}
			employee.setPhoneNumbers(employeeDTO.getPhoneNumbers());
		}
		try {
			Date date = new SimpleDateFormat("yyyy-MM-dd").parse(employeeDTO.getDoj().toString());
		} catch (ParseException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date format");
		}
		employee.setDoj(employeeDTO.getDoj());
		employee.setSalary(employeeDTO.getSalary());
		try {
			employeeRepository.save(employee);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}

		return employee;
	}

	private boolean isNumber(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (!Character.isDigit(s.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public TaxDeductionDTO getTaxDeduction(String employeeId) throws ParseException {
		Employee employee = getEmployeeById(employeeId);
		Double empSalary = employee.getSalary();
		Double totalTax = getTax(empSalary);

//        Date date=new SimpleDateFormat("yyyy-MM-dd").parse(employee.getDoj());
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//        int month = cal.get(Calendar.MONTH) + 1;


		double cessTax = 0.0;
		if (empSalary > 2500000) {
			cessTax = 0.02 * (empSalary - 2500000);
		}

		TaxDeductionDTO taxDeductionDTO = new TaxDeductionDTO();
		taxDeductionDTO.setEmployeeId(employee.getEmployeeId());
		taxDeductionDTO.setFirstName(employee.getFirstName());
		taxDeductionDTO.setLastName(employee.getLastName());
		taxDeductionDTO.setYearlySalary(empSalary);
		taxDeductionDTO.setTaxAmount(totalTax);
		taxDeductionDTO.setCessAmount(cessTax);
		return taxDeductionDTO;
	}

	private Double getTax(Double empSalary) {
		double tax;
		if (empSalary <= 250000) {
			tax = 0.0;
		} else if (empSalary > 250000 && empSalary <= 500000) {
			tax = 0.05 * (empSalary - 250000);
		} else if (empSalary > 500000 && empSalary <= 1000000) {
			tax = 0.05 * (250000) + 0.1 * (empSalary - 500000);
		} else {
			tax = 0.05 * (250000) + 0.1 * (500000) + 0.2 * (empSalary - 1000000);
		}
		System.out.println("Tax calculated is: " + tax);
		logger.debug("Tax calculated is: " + tax);
		return tax;
	}

	private Employee getEmployeeById(String employeeId) {
		Optional<Employee> employeeOptional = employeeRepository.findByEmployeeId(employeeId);
		if (employeeOptional.isPresent()) {
			return employeeOptional.get();
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid employee id");
		}
	}
}

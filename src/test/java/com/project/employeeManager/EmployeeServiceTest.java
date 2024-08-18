package com.project.employeeManager;

import com.project.employeeManager.dto.EmployeeDTO;
import com.project.employeeManager.entity.Employee;
import com.project.employeeManager.repository.EmployeeRepository;
import com.project.employeeManager.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

	@Mock
	EmployeeRepository employeeRepository;

	@InjectMocks
	EmployeeService employeeService;

	@Test
	void testCreateEmployee() {
		List<String> phoneNumberList = new ArrayList<>();
		phoneNumberList.add("9876543210");

		EmployeeDTO employeeDTO = getEmployeeDTO(phoneNumberList);

		Employee employee = getEmployee(employeeDTO);

		lenient().when(employeeRepository.save(employee)).thenReturn(employee);
		assertEquals(employee.getFirstName(), employeeService.createEmployee(employeeDTO).getFirstName());

		//number with alphabets
		phoneNumberList.add("987654321q");
		assertThrows(ResponseStatusException.class, () -> employeeService.createEmployee(employeeDTO));

		phoneNumberList.remove("987654321q");

		//invalid number length
		phoneNumberList.add("0987654321");
		assertThrows(ResponseStatusException.class, () -> employeeService.createEmployee(employeeDTO));

		//empty phoneNumberList
		phoneNumberList.clear();
		assertThrows(ResponseStatusException.class, () -> employeeService.createEmployee(employeeDTO));
	}

	@Test
	void testGetTaxDeduction() throws ParseException {
		List<String> phoneNumberList = new ArrayList<>();
		phoneNumberList.add("9876543210");

		Employee employee = getEmployee(getEmployeeDTO(phoneNumberList));

		//empId is not present
		lenient().when(employeeRepository.findByEmployeeId(employee.getEmployeeId())).thenReturn(Optional.empty());
		assertThrows(ResponseStatusException.class, () -> employeeService.getTaxDeduction(employee.getEmployeeId()));

		//valid employee
		lenient().when(employeeRepository.findByEmployeeId(employee.getEmployeeId())).thenReturn(Optional.of(employee));
		assertEquals(112500, employeeService.getTaxDeduction(employee.getEmployeeId()).getTaxAmount());

		employee.setSalary(250000.0);
		lenient().when(employeeRepository.findByEmployeeId(employee.getEmployeeId())).thenReturn(Optional.of(employee));
		assertEquals(0.0, employeeService.getTaxDeduction(employee.getEmployeeId()).getTaxAmount());

		employee.setSalary(350000.0);
		lenient().when(employeeRepository.findByEmployeeId(employee.getEmployeeId())).thenReturn(Optional.of(employee));
		assertEquals(5000.0, employeeService.getTaxDeduction(employee.getEmployeeId()).getTaxAmount());

		employee.setSalary(750000.0);
		lenient().when(employeeRepository.findByEmployeeId(employee.getEmployeeId())).thenReturn(Optional.of(employee));
		assertEquals(37500.0, employeeService.getTaxDeduction(employee.getEmployeeId()).getTaxAmount());

		employee.setSalary(1250000.0);
		lenient().when(employeeRepository.findByEmployeeId(employee.getEmployeeId())).thenReturn(Optional.of(employee));
		assertEquals(112500, employeeService.getTaxDeduction(employee.getEmployeeId()).getTaxAmount());

		employee.setSalary(2500001.0);
		lenient().when(employeeRepository.findByEmployeeId(employee.getEmployeeId())).thenReturn(Optional.of(employee));
		assertEquals(362500.2, employeeService.getTaxDeduction(employee.getEmployeeId()).getTaxAmount());
		assertEquals(0.02, employeeService.getTaxDeduction(employee.getEmployeeId()).getCessAmount());

	}

	private Employee getEmployee(EmployeeDTO employeeDTO) {

		Employee employee = new Employee();
		employee.setEmployeeId(employeeDTO.getEmployeeId());
		employee.setFirstName(employeeDTO.getFirstName());
		employee.setLastName(employeeDTO.getLastName());
		employee.setEmail(employeeDTO.getEmail());
		employee.setPhoneNumbers(employeeDTO.getPhoneNumbers());
		employee.setDoj(employeeDTO.getDoj());
		employee.setSalary(employeeDTO.getSalary());

		return employee;
	}

	private EmployeeDTO getEmployeeDTO(List<String> phoneNumberList) {
		EmployeeDTO employeeDTO = new EmployeeDTO();
		employeeDTO.setEmployeeId("E123");
		employeeDTO.setFirstName("Aakash");
		employeeDTO.setLastName("P");
		employeeDTO.setEmail("aakash@gmail.com");
		employeeDTO.setPhoneNumbers(phoneNumberList);
		employeeDTO.setDoj(LocalDate.now());
		employeeDTO.setSalary(1250000.0);

		return employeeDTO;
	}
}

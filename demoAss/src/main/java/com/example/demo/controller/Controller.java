package com.example.demo.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.demo.config.jwt.JwtService;
import com.example.demo.model.Doctor;
import com.example.demo.model.Patients;
import com.example.demo.model.Schedules;
import com.example.demo.model.Specializations;
import com.example.demo.model.User;
import com.example.demo.modelDTO.EmailDTO;
import com.example.demo.modelDTO.SchedulesDTO;
import com.example.demo.modelDTO.UserDTO;
import com.example.demo.services.DoctorService;
import com.example.demo.services.MailService;
import com.example.demo.services.PatientsService;
import com.example.demo.services.SchedulesService;
import com.example.demo.services.SpecializationsService;
import com.example.demo.services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/")
public class Controller {
	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private MailService mailService;

	@Autowired
	private DoctorService doctorService;

	@Autowired
	private PatientsService patientsService;

	@Autowired
	private UserService userService;

	@Autowired
	private SpecializationsService specializationsService;

	@Autowired
	private SchedulesService schedulesService;

	private Authentication authentication;
	private SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

	public void userLogin() {
		String nameString = authentication.getName();
		authentication.getName();
		authentication.getDetails().toString();
	}

	public User userLoginIs(Authentication authentication) {
		String nameString = authentication.getName();
		authentication.getName();
		authentication.getDetails().toString();
		return userService.getUserByEmail(nameString);
	}

//	///////////////////////////////////////////////////////////////////////////////////
	
//	dang nhap - lay ma token
	@PostMapping("/authenticate")
	public String authenticateAndGetToken(@RequestBody User user) {
		/* System.out.println(patientsService.patientsbyDoctor(0).size()); */
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
		if (authentication.isAuthenticated()) {
			return jwtService.generateToken1(user.getEmail());
		} else {
			throw new UsernameNotFoundException("invalid user request !");
		}
	}
	
//	 dang ki tai khoan
	@PostMapping("/public/regisacount")
	public ResponseEntity<User> regis(@RequestBody User user) {
		if (userService.AddUser(user)) {
			System.out.println(user.toString());
			return ResponseEntity.status(200).body(user);
		}
		return ResponseEntity.status(404).body(null);
	}

// quen mat khau 
	@PostMapping("/public/forgotPassWord")
	public ResponseEntity<String> forgotPassWord(@RequestBody EmailDTO emailDTO) {
		Optional<User> user = userService.getUserByEmailOptional(emailDTO.getTo());

		if (user.isEmpty()) {
			return ResponseEntity.status(406).body("email k ton tai");
		} else {
			String head = "dat lai mk";
			String content = "link dan: http://localhost:8080/user/forgotPassWord/?email=" + emailDTO.getTo() + "&pass="
					+ emailDTO.newPass;
			boolean isMultipart = true;
			boolean isHtml = false;
			String path = null;
			mailService.sendEmail(emailDTO.getTo(), head, content, isMultipart, isHtml, path);
			return ResponseEntity.status(200).body("vao email "+emailDTO.getTo()+" de xac thuc mk");
		}
	}
//	xac thuc mk qua email
	@GetMapping("/public/confirmPass")
	public ResponseEntity<String> confirmPass(@RequestParam(name = "pass") String newPass,
			@RequestParam(name = "email") String email) {
		userService.setPass(newPass, email);
		return ResponseEntity.status(200).body("cap nhat thanh cong");
	}

// hien thi thong tin ca nhan
	@GetMapping("/public/personalInfor")
	public User personalInfor(Authentication authentication) {
		String nameString = authentication.getName();
		authentication.getName();
		authentication.getDetails().toString();
		return userService.getUserByEmail(nameString);
	}

//	hien thi chuyen khoa noi bat
	@GetMapping("/public/outStandingSpecializations")
	public Specializations OutStandingSpecializations() {
		Specializations specializations = new Specializations();
		specializations = specializationsService.OutstandingSpecial();
		return specializations;
	}

//	Tìm kiếm theo chuyên khoa của bác sĩ
	@GetMapping("/public/SearchSpecializations")
	public List<Doctor> SearchSpecializations(@RequestBody String keySearch) {
		List<Doctor> list = new ArrayList<Doctor>();
		list = doctorService.findByspecializations(keySearch);
		return list;
	}
//	Tim kiem chung

//	Dat lich kham
	@PostMapping("/user/createSchedules")
	public ResponseEntity<SchedulesDTO> createSchedule(@RequestBody SchedulesDTO schedulesDTO) {
		patientsService.addSchedules(schedulesDTO);
		Doctor doctor = doctorService.findById(schedulesDTO.getDoctorId());
		System.out.println(doctor.getSchedules().size() + "so luong ");
		return ResponseEntity.status(200).body(schedulesDTO);
	}

	// danh sach benh nhan cua bac si
	@GetMapping("/doctor/listPatients")
	public List<Patients> listPatients(Authentication authentication1) {

//		User user = userService.getUserByEmail(authentication.getName());
		User user = userLoginIs(authentication1);
		List<Patients> list = new ArrayList<Patients>();
		try {
			int id = user.getId();
			Doctor doctor = doctorService.doctorFromUser(id);
			List<Schedules> listSchedules = doctor.getSchedules();
			for (Schedules schedules : listSchedules) {
				list.add(schedules.getPatients());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return list;
	}

//	Nhận/hủy lịch khám của bệnh nhân
	@PostMapping("/admin/confirmSchedules")
	public ResponseEntity<SchedulesDTO> confirmSchedules(@RequestBody SchedulesDTO schedulesDTO) {
		schedulesService.confirm(schedulesDTO.getStatus(), schedulesDTO.getDescription(), schedulesDTO.getId());
		return ResponseEntity.status(200).body(schedulesDTO);
	}

//	 Khóa/hủy khóa tài khoản của bệnh nhân/bac si
	@PostMapping("/admin/statusUser")
	public ResponseEntity<User> lockUser(@RequestBody User user) {
		userService.setStatus(user);
		return ResponseEntity.status(200).body(user);
	}

	public ResponseEntity<User> lockUser1(@RequestBody int userId) {
		userService.setStatus(userId);
		return ResponseEntity.status(200).body(userService.getUserById(userId));
	}

//	Thêm tài khoản của bác sĩ
	@PostMapping("/admin/addDoctor")
	public ResponseEntity<UserDTO> addDoctor(@RequestBody UserDTO userDTO) {
		if (userService.AddUser(userDTO)) {
			return ResponseEntity.status(200).body(userDTO);
		} else {
			return ResponseEntity.status(406).body(userDTO);
		}
	}

//	Gửi thông tin về email cá nhân của bệnh nhân
	@PostMapping("/doctor/sendMail")
	public String sendMail(@RequestBody EmailDTO emailDTO) {
		EmailDTO emailDTO2;
		mailService.sendEmail(emailDTO.getTo(), emailDTO.getSubject(), emailDTO.getContent(), emailDTO.isMultipart(),
				emailDTO.isHtml(), emailDTO.getPath());
		return "test";
	}

//  Admin Xem thông tin chi tiết lịch khám của từng bệnh nhân
	@GetMapping("/admin/schedulesPatients")
	public ResponseEntity<List<Schedules>> schedulePatients(@RequestBody int patientsId) {
		Patients patients = patientsService.getPatientsById(patientsId);
		List<Schedules> list = patients.getSchedules();
		return ResponseEntity.status(200).body(list);
	}

//	Admin Xem thông tin chi tiết lịch khám của từng bác sĩ
	@GetMapping("/admin/schedulesDoctor/{doctorId}")
	public ResponseEntity<List<Schedules>> scheduleDoctor(@PathVariable int doctorId) {
		Doctor doctor = doctorService.findById(doctorId);
		List<Schedules> list = doctor.getSchedules();
		return ResponseEntity.status(200).body(list);
	}
//	//////////////////////////////////////////////////////////////////////////////////////////////////

}

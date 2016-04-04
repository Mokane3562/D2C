/**
 * Every service you use will need to be injected.
 * To do so, add the name as a string to the start of the array,
 * and add the name as a variable to the function.
 */
app.controller('main_controller',['$scope', '$location', 'example_service', 'c_compile_request',
                                  'j_compile_request', 'java_request', 'run_request', 'login_request',
                                  'signup_service', 'transferable_account', 'dir_creator', 'roles_request', 
                                  'course_instance_request', 'course_inst_by_id_request', 'course_by_id_request', 
                                  'file_request',
                          function($scope, $location, example_service, c_compile_request, j_compile_request,
                        		  java_request, run_request, login_request, signup_service, transferable_account, dir_creator,
                        		  roles_request, course_instance_request, course_inst_by_id_request, course_by_id_request, file_request){
	console.log("main controller loading");
	
    //initial view object set up	
	var view = {};
	var editor;
	//used for getting info from roles
	var role_list = {};
	var course_inst = [];
	//the view
	$scope.view = view;
	$scope.login_failure = "";
	$scope.user = "";
	$scope.first = "";
	$scope.last = "";
	$scope.confirm = "";
	$scope.password = "";
	$scope.dirs = [];
	$scope.dir = "";
	$scope.roles = [];
	$scope.courseInst = [];
	$scope.courseLookUp = [];
	$scope.courseResults = [];
	$scope.get_the_files = [];
	$scope.finalResults = [];
	var fileId = 4;
	

	//Setting view invisible
	view["login"] = true;
	view["signup"] = false;
	view["firstNav"] = false;
	view["courses"] = false;
	view["register"] = false;
	view["assignments"] = false;
	view["workspace"] = false;
	view["path"] = false;
	view["testing"] = false;	
	view["submissions"] = false;
	view["grades"] = false;
	view["signout"] = false;
	view["results"] = false;
	var user_auth = "";
	var crn = "";
	
	//Setting view visible on click
	$scope.login = function(){
		console.log("login event triggered");
		var to_encode = $scope.user+":"+$scope.password;
		console.log($scope.user);
		console.log($scope.password);
		console.log(to_encode);
		var auth = window.btoa(to_encode);
		console.log(auth);
		login_request(auth).then(
			function(response){
				user_auth = auth;
				view["login"] = false;
				view["signup"] = false;
				view["courses"] = true;
				view["firstNav"] = true;
				view["register"] = false;
				view["assignments"] = false;
				view["workspace"] = false;
				view["testing"] = false;	
				view["submissions"] = false;
				view["grades"] = false;
				view["signout"] = false;
				view["results"] = false;
				rolesRequest();
				getFile();
		},
		function(errors){
			console.log(errors);
			if(errors.status === 403){
				$scope.login_failure = "invalid username/password";
			} else {
				$scope.login_failure = "Server problems, please contact administrator";
			}
		});
	}
	//TODO: this is currently broken. probably some async shit 
	var rolesRequest = function (){
		var to_encode = $scope.user+":"+$scope.password;
		var auth = window.btoa(to_encode);
		$scope.courses_and_roles = [];
		//execute roles service on the user info
		roles_request(auth, $scope.user).then(
			function (response){
				//save the role list info
				role_list = response.data;
				console.log(role_list);
				for (var course_inst_id in role_list){//for each course instance id in the role list
					if (!role_list.hasOwnProperty(course_inst_id)) {
					    continue;
					}
					//execute course instance by id service on the course instance id
					course_inst_by_id_request(course_inst_id).then(
						function(course_inst_response){
							//save the course instance info
							course_inst = course_inst_response.data;
							//execute course by id service on the course id given from the course instance
							course_by_id_request(course_inst_response.data.refID).then(
								function(course_response){
									//console.log("course_response");
									course_response.data.role = role_list[course_inst_id];
									course_response.data.clickHandler = function clickHandler(){
										
									}
									$scope.courses_and_roles.push(course_response.data);
								}
							);
						}
					);
				}
			},
			function(errors){
				console.log("failure");
				//TODO deal with the error
			}
		);
	} 
	
	$scope.lookUpCourses = function(){
		console.log($scope.crn);
		course_instance_request($scope.crn).then(
			function(response){
				console.log(response.data);
				$scope.courseLookUp = response.data;
				console.log($scope.courseLookUp.year);
				results();
				course_by_id_request(response.data.courseID).then(
					function(course_response){
						$scope.coName = (course_response.data.name);
						$scope.coSub = (course_response.data.subject);
						$scope.coNum = (course_response.data.number);
						$scope.coPro = (response.data.profName);
						$scope.coSem = (response.data.semester);
						$scope.coYea = (response.data.year);
						console.log($scope.finalResults);
					}
				);
			},
			function(errors){
				console.log("failure");
			}
		);
	}
	
	var getAssignments = function(){
		var to_encode = $scope.user+":"+$scope.password;
		var auth = window.btoa(to_encode);
		$scope.assignments = [];
		
		course_assignments_request(crn).then(
			function(response){
				assignmentS = response.data;
				console.log(response.data);				
				for(var assignNumber in assignmentS){
					if(!assignmentS.hasOwnProperty(assignNumber)){
						continue;
					}
					console.log(assignmentS[assignNumber]);
					assignment_by_id_request(assignmentS[assignNumber]).then(//assignment_by_id_request
							function(assign_response){
								$scope.assignments.push(assign_response.data);
							});
				}
				console.log(assignments);
			},
			function(errors){
				console.log("failure");
			}
		);
	}
	
	var getFile = function(){
		var to_encode = $scope.user+":"+$scope.password;
		var auth = window.btoa(to_encode);
			file_request(fileId).then(
					function(response){
						console.log(response.data);
						$scope.get_the_files = (response.data.fileName);
						console.log($scope.get_the_files);
					},
					function(errors){
						console.log("failblog");
					}
			);
	}
	var results = function(){
		view["login"] = false;
		view["signup"] = false;
		view["courses"] = false;
		view["firstNav"] = false;
		view["register"] = false;
		view["workspace"] = false;
		view["testing"] = false;	
		view["submissions"] = false;
		view["grades"] = false;
		view["signout"] = false;
		view["results"] = true;
	}
	
	$scope.signUp = function(){
		view["login"] = false;
		view["signup"] = true;
		view["courses"] = false;
		view["register"] = false;
		view["assignments"] = false;
		view["workspace"] = false;
		view["testing"] = false;	
		view["submissions"] = false;
		view["grades"] = false;
		view["signout"] = false;
		view["results"] = false;
	}
	$scope.signUpUser =function(){
		if($scope.password !== $scope.confirm){
			return
		}
		var account = transferable_account(
				$scope.user,
				$scope.password,
				$scope.first,
				$scope.last,
				new Date()
		);
		signup_service(account).then(
			function(response){
				$scope.login();
			},
			function(response){
				$scope.login_failure = "";
			}
		);
	}
	$scope.back = function(){
		view["login"] = true;
		view["signup"] = false;
		view["courses"] = false;
		view["register"] = false;
		view["assignments"] = false;
		view["workspace"] = false;
		view["testing"] = false;	
		view["submissions"] = false;
		view["grades"] = false;
		view["welcome"] = false;
		view["signout"] = false;
		view["results"] = false;
	}
	$scope.courses_click = function(){
		view["login"] = false;
		view["register"] = false;
		view["firstNav"] = true;
		view["signup"] = false;
		view["courses"] = true;
		view["register"] = false;
		view["assignments"] = false;
		view["workspace"] = false;
		view["testing"] = false;	
		view["submissions"] = false;
		view["grades"] = false;
		view["welcome"] = false;
		view["signout"] = false;
		view["results"] = false;
	}
	$scope.register_click = function(){
		view["login"] = false;
		view["register"] = false;
		view["signup"] = false;
		view["courses"] = false;
		view["register"] = true;
		view["assignments"] = false;
		view["workspace"] = false;
		view["testing"] = false;	
		view["submissions"] = false;
		view["grades"] = false;
		view["welcome"] = false;
		view["signout"] = false;
		view["results"] = false;
	}

	$scope.assignments_click = function(){
		view["login"] = false;
		view["signup"] = false;
		view["courses"] = false;
		view["register"] = false;
		view["assignments"] = true;
		view["workspace"] = false;
		view["firstNav"] = false;
		view["testing"] = false;	
		view["submissions"] = false;
		view["grades"] = false;
		view["welcome"] = false;
		view["signout"] = false;
		view["results"] = false;
	}
	$scope.workspace_click = function(){
		view["login"] = false;
		view["signup"] = false;
		view["courses"] = false;
		view["register"] = false;
		view["assignments"] = false;
		view["workspace"] = true;
		view["path"] = true;
		view["testing"] = false;	
		view["submissions"] = false;
		view["grades"] = false;
		view["welcome"] = false;
		view["signout"] = false;
		view["results"] = false;
		editor = ace.edit("textcodebox");
		editor.getSession().setUseWorker(false);
		editor.setTheme("ace/theme/twilight");
		editor.getSession().setMode("ace/mode/java");
	}
	$scope.testing_click = function(){
		view["login"] = false;
		view["signup"] = false;
		view["courses"] = false;
		view["register"] = false;
		view["assignments"] = false;
		view["workspace"] = false;
		view["results"] = false;
		view["testing"] = true;	
		view["submissions"] = false;
		view["grades"] = false;
		view["welcome"] = false;
		view["signout"] = false;
	}
	$scope.submissions_click = function(){
		view["login"] = false;
		view["signup"] = false;
		view["courses"] = false;
		view["register"] = false;
		view["assignments"] = false;
		view["workspace"] = false;
		view["testing"] = false;	
		view["results"] = false;
		view["submissions"] = true;
		view["grades"] = false;
		view["welcome"] = false;
		view["signout"] = false;
	}
	$scope.grades_click = function(){
		view["login"] = false;
		view["signup"] = false;
		view["courses"] = false;
		view["register"] = false;
		view["assignments"] = false;
		view["results"] = false;
		view["workspace"] = false;
		view["testing"] = false;	
		view["submissions"] = false;
		view["grades"] = true;
		view["welcome"] = false;
		view["signout"] = false;
	}
	
	$scope.signout_click = function(){
		view["login"] = false;
		view["signup"] = false;
		view["courses"] = false;
		view["register"] = false;
		view["assignments"] = false;
		view["results"] = false;
		view["workspace"] = false;
		view["testing"] = false;	
		view["submissions"] = false;
		view["grades"] = false;
		view["welcome"] = false;
		view["signout"] = true;
	}
	
	$scope.yes_click = function(){
		view["login"] = true;
		view["signup"] = false;
		view["courses"] = false;
		view["register"] = false;
		view["assignments"] = false;
		view["workspace"] = false;
		view["results"] = false;
		view["testing"] = false;	
		view["submissions"] = false;
		view["grades"] = false;
		view["welcome"] = false;
		view["signout"] = false;
	}
	
	//START c_compile_function
	$scope.c_compile_function = function(){
		var absolute_path = document.getElementById("path").value;
		var path_array = absolute_path.split("/");
		var name = path_array[path_array.length-1];
		path_array.pop();
		var path = path_array.join("/");
		var code = {};
		code[name]= editor.getValue();
		var user = $scope.user;
		c_compile_request(code, user, path).then(
			function(response){
				console.log("success");
				$scope.output = response.data;
			},
			function(errors){
				console.log("failure");
				//TODO deal with the error
			}
		);
		console.log("c_compile_function starting");
	};
	//END c_compile_function
	
	//START j_compile_function
	
	$scope.j_compile_function = function(){
		var absolute_path = document.getElementById("path").value;
		var path_array = absolute_path.split("/");
		var name = path_array[path_array.length-1];
		path_array.pop();
		var path = path_array.join("/");
		var code = {};
		code[name]= editor.getValue();
		var user = $scope.user;
		j_compile_request(code, user, path).then(
			function(response){
				console.log("success");
				$scope.output = response.data;
			},
			function(errors){
				console.log("failure");
				//TODO deal with the error
			}
		);
		console.log("j_compile_function starting");
	};
	
	//END of j_compile_function
	
	$scope.java_function = function(){
		var absolute_path = document.getElementById("path").value;
		var path_array = absolute_path.split("/");
		var name = path_array[path_array.length-1];
		path_array.pop();
		var path = path_array.join("/");
		var code = {};
		code[name]= "";
		var user = $scope.user;
		java_request(code, user, path).then(
			function(response){
				console.log("success");
				$scope.output = response.data;
			},
			function(errors){
				console.log("failure");
				$scope.output = errors.data;
			}
		);
		console.log("java_function starting");
	};
	
	$scope.aout_function = function(){
		var absolute_path = document.getElementById("path").value;
		var path_array = absolute_path.split("/");
		var name = path_array[path_array.length-1];
		path_array.pop();
		var path = path_array.join("/");
		var code = {};
		code[name]= "";
		var user = $scope.user;
		run_request(code, user, path).then(
			function(response){
				console.log("success");
				$scope.output = response.data;
			},
			function(errors){
				console.log("failure");
				//TODO deal with the error
			}
		);
		console.log("aout_function starting");
	};

	console.log("main controller loaded");

	console.log("main controller loaded");
	
	//END MENUBAR GRABBER
	
}]);
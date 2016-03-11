/**
 * Every service you use will need to be injected.
 * To do so, add the name as a string to the start of the array,
 * and add the name as a variable to the function.
 */
app.controller('main_controller',['$scope', '$location', 'example_service', 'c_compile_request', 'j_compile_request',
                                  'java_request', 'run_request',
                          function($scope, $location, example_service, c_compile_request, j_compile_request,
                        		  java_request, run_request){
	console.log("main controller loading");
	
    //intial veiw object set up	
	view = {};
	$scope.view = view;

	//Setting view invisible
	view["courseInfo"] = false;
	view["assignments"] = false;
	view["workspace"] = false;
	view["testing"] = false;	
	view["submissions"] = false;
	view["grades"] = false;
	
	//Setting view visible on click
	
	$scope.courseInfo_click = function(){
		view["courseInfo"] = true;
		view["assignments"] = false;
		view["workspace"] = false;
		view["testing"] = false;	
		view["submissions"] = false;
		view["grades"] = false;
	}
	$scope.assignments_click = function(){
		view["courseInfo"] = false;
		view["assignments"] = true;
		view["workspace"] = false;
		view["testing"] = false;	
		view["submissions"] = false;
		view["grades"] = false;
	}
	$scope.workspace_click = function(){
		view["courseInfo"] = false;
		view["assignments"] = false;
		view["workspace"] = true;
		view["testing"] = false;	
		view["submissions"] = false;
		view["grades"] = false;
	}
	$scope.testing_click = function(){
		view["courseInfo"] = false;
		view["assignments"] = false;
		view["workspace"] = false;
		view["testing"] = true;	
		view["submissions"] = false;
		view["grades"] = false;
	}
	$scope.submissions_click = function(){
		view["courseInfo"] = false;
		view["assignments"] = false;
		view["workspace"] = false;
		view["testing"] = false;	
		view["submissions"] = true;
		view["grades"] = false;
	}
	$scope.grades_click = function(){
		view["courseInfo"] = false;
		view["assignments"] = false;
		view["workspace"] = false;
		view["testing"] = false;	
		view["submissions"] = false;
		view["grades"] = true;
	}
	
	//START c_compile_function
	$scope.c_compile_function = function(){
		var absolute_path = document.getElementById("path").value;
		var path_array = absolute_path.split("/");
		var name = path_array[path_array.length-1];
		path_array.pop();
		var path = path_array.join("/");
		var code = {};
		code[name]= document.getElementById("textcodebox").value;
		var user = document.getElementById("user_name").value;
		c_compile_request(code, user, path).then(
			function(response){
				console.log("success");
				$scope.output = response.data;
			},
			function(errors){
				console.log("failure");
				$scope.output = errors.data;
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
		code[name]= document.getElementById("textcodebox").value;
		var user = document.getElementById("user_name").value;
		j_compile_request(code, user, path).then(
			function(response){
				console.log("success");
				$scope.output = response.data;
			},
			function(errors){
				console.log("failure");
				$scope.output = errors.data;
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
		var user = document.getElementById("user_name").value;
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
		var user = document.getElementById("user_name").value;
		run_request(code, user, path).then(
			function(response){
				console.log("success");
				$scope.output = response.data;
			},
			function(errors){
				console.log("failure");
				$scope.output = errors.data;
			}
		);
		console.log("aout_function starting");
	};
	
	//COURSE LIST GRABBER
	$scope.courseList = [];
	$scope.getCourseList = function(){
		var user = document.getElementById("user_name").value;
		var password = document.getElementById("user_name").value;
		getMeMYCourses(user, password).then(
				function(response){
					$scope.courseList = response.data.courseList;
				},
				function(errors){
					//TODO deal with the error
				}
		);
	}
	$scope.courseHandler = function(course){
		//TODO something fucking useful with a course i guess?
	}
	console.log("main controller loaded");
	
	//END COURSE LIST GRABBER
	
	//MENUBAR GRABBER
	
	$scope.menuList = [];
	$scope.getMenuList = function(){
		var user = document.getElementById("user_name").value;
		var password = document.getElementById("user_name").value;
		getMeMYMenubar(user, password).then(
				function(response){
					$scope.menuList = response.data.menuList;
				},
				function(errors){
					//TODO deal with the error
				}
		);
	}
	$scope.menuHandler = function(menu){
		//TODO something fucking useful with a course i guess?
	}
	console.log("main controller loaded");
	
	//END MENUBAR GRABBER
	
}]);
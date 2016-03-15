app.factory('transferable_role', [function(){
	return function(account, course_instance, role){
		return {
			'account': account,
			'course_instance': course_instance,
			'role': role
		};
	};
}]);
app.factory('transferable_course_instance',[function(){
	return function(year_offered, semester, crn, course){
		return {
			'year_offered': year_offered,
			'semester': semester,
			'crn': crn,
			'course': course
		};
	};
}]);
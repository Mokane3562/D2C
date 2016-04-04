app.factory('transferable_course_instance',[function(){
	return function(year_offered, semester, crn, course){
		return {
			'semester': semester,
			'year_offered': year,
			'profName': profName,
			'crn': courseReferenceNumber,
			'courseID': courseID,
			'refID':refID
		};
	};
}]);
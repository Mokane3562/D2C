app.factory('transferable_course',[function(){
	return function(subject, number, name){
		return {
			'subject': subject,
			'number': number,
			'name': name
		};
	};
}]);
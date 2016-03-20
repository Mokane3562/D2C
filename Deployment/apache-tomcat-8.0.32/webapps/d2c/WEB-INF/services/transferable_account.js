app.factory('transferable_account', [function(){
	return function(userName, firstName, lastName, createTime){
		return {
			'userName': userName,
			'firstName': firstName,
			'lastName': lastName,
			'createTime': createTime
		};
	};
}]);
app.factory('transferable_file', [function(){
	return function(name, type, contents, date_added, account){
		return {
			'name': name,
			'type': type,
			'contents': contents,
			'date_added': date_added,
			'account': account
		};
	};
}]);
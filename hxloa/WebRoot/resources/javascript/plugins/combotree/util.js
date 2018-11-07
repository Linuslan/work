function remove(array, obj) {
	if(array && 0 < array.length) {
		var a = array.indexOf(obj);
		if(a >= 0) {
			array.splice(a, 1);
			return true;
		}
	}
	return false;
}
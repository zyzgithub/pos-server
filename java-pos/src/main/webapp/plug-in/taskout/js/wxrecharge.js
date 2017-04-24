function WxRecharge() {
	this.data = {};
	this.basePath;
	this.submiting = false;

	this.init = function(basePath) {
		this.basePath = basePath;
	};
	
	// 千分位处理函数
	this.departNum = function(textVal, the_other) {
		var the_array = [];
		var i = 0;
		the_array.push(textVal.slice(textVal.length - 2, textVal.length));
		for (i = textVal.length - 5; i >= 0; i -= 3) {
			the_array.push(textVal.slice(i, i + 3));
		}
		if (0 - i < 3) {
			the_array.push(textVal.slice(0, 3 + i));
		}
		for (var k = the_array.length - 1; k >= 0; k--) {
			the_other.push(the_array[k]);
		}
	}

	// 金额输入限制
	this.amountInput = function(elementIdOrClass) {
		$(elementIdOrClass)
				.keypress(
						function(event) {
							var current = $(this).val();
							if (event.keyCode
									&& (event.keyCode < 45
											|| (event.keyCode > 45 && event.keyCode < 48) || event.keyCode > 57)) {
								if (event.keyCode == 46 && !/\./.test(current)) {
									if (!isNaN(parseInt($(this).val().replace(
											/,/, "")))) {
										$(this).val(current + ".");
									} else {
										$(this).val($(this).val() + "0.");
									}
								}
								event.preventDefault();
							} else {
								if (event.keyCode == 45 && /-/.test(current)) {
									event.preventDefault();
								} else if (event.keyCode != 45) {
									if (!/\./.test(current)) {
										var the_new = $(this).val().replace(
												/,/g, "");
										var theArray = [];
										var theFlag = "";
										if (/-/.test(current)) {
											theFlag = the_new.slice(0, 1);
											the_new = the_new.slice(1);
										}
										if (parseInt(the_new) >= 100) {
											wxRecharge.departNum(the_new, theArray);
											$(this).val(
													theFlag
															+ theArray
																	.join(","));
										}
									}
								}
							}
						}).keyup(
						function(event) {
							if (event.keyCode == 109
									&& $(this).val().slice(0, 1) != "-") {
								var the_Real = $(this).val();
								$(this).val(the_Real.replace(/-/, ""));
							}
						}).blur(
						function() {
							var the_Val = $(this).val().replace(/,/g, "");
							if (!isNaN(parseFloat(the_Val))) {
								if (!/\./.test(the_Val)) {
									var theArray = [];
									var theFlag = "";
									var the_one = the_Val.slice(-1);
									var the_new = the_Val.replace(/\d$/, "");
									if (/-/.test(the_Val)) {
										theFlag = the_new.slice(0, 1);
										the_new = the_new.slice(1);
									}
									if (parseInt(the_new) >= 100) {
										wxRecharge.departNum(the_new, theArray);
										$(this).val(
												theFlag + theArray.join(",")
														+ the_one + ".00");
									} else {
										$(this).val(the_Val + ".00");
									}
								} else {
									var theArray = [];
									var theFlag = "";
									var the_now = parseFloat(the_Val)
											.toFixed(2);
									var the_nowStr = String(the_now).slice(-4);
									var the_new = String(the_now).replace(
											/\d\.\d\d/, "");
									if (/-/.test(the_Val)) {
										theFlag = the_new.slice(0, 1);
										the_new = the_new.slice(1);
									}
									if (parseInt(the_new) >= 100) {
										wxRecharge.departNum(the_new, theArray);
										$(this).val(
												theFlag + theArray.join(",")
														+ the_nowStr);
									} else {
										$(this).val(String(the_now));
									}
								}
							}
						});
	}
}
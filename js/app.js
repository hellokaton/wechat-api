/*
原理： 1.把所有的li的高度值放到数组里面
     2.第一行的top都为0
     3.计算高度值最小的值是哪个li
     4.把接下来的li放到那个li的下面
*/
var margin = 10; //设置间距
var li = $(".product_list"); //区块名称
var li_W = li[0].offsetWidth + margin; //取区块的实际宽度
function liuxiaofan() {
	var h = []; //记录区块高度的数组
	var n = 960 / li_W | 0;
	for (var i = 0; i < li.length; i++) {
		li_H = li[i].offsetHeight; //获取每个li的高度
		if (i < n) { //n是一行最多的li，所以小于n就是第一行了
			max_H = Math.max.apply(null, h);
			h[i] = li_H; //把每个li放到数组里面
			li.eq(i).css("top", 0); //第一行的Li的top值为0
			li.eq(i).css("left", i * li_W); //第i个li的左坐标就是i*li的宽度
		} else {
			min_H = Math.min.apply(null, h); //取得数组中的最小值，区块中高度值最小的那个
			minKey = getarraykey(h, min_H); //最小的值对应的指针
			h[minKey] += li_H + margin; //加上新高度后更新高度值
			li.eq(i).css("top", min_H + margin); //先得到高度最小的Li，然后把接下来的li放到它的下面
			li.eq(i).css("left", minKey * li_W); //第i个li的左坐标就是i*li的宽度
		}
		//  $("p").eq(i).text("高度："+li_H);//把区块高度值写入对应的区块H2标题里面
	}
	max = Math.max.apply(null, h);
	$("#con1_1").css("height", max);
}

/* 
 * 使用for in运算返回数组中某一值的对应项数(比如算出最小的高度值是数组里面的第几个) 
 */
function getarraykey(s, v) {
	for (k in s) {
		if (s[k] == v) {
			return k;
		}
	}
}

/*
 * 这里一定要用onload，因为图片不加载完就不知道高度值
 */
window.onload = function() {
	liuxiaofan();
};

window.onresize = function() {
	liuxiaofan();
};

//鼠标在上样式
$(function() {
	$(".product_list").hover(function() {
		$(this).css("background-color", "#ddd");
	}, function() {
		$(this).css("background-color", "#eee");
	});
});
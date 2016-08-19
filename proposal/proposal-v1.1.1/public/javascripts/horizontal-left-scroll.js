jQuery(function($) {

    $.putMyDesign = function( htmlObj,isObj ) {
        if(isObj){
            var $target = $($(htmlObj).attr('href')),
                $other = $target.siblings('.content, .panel');
        }else{
            var $target = $(htmlObj),
                $other = $target.siblings('.content, .panel');
        }
        
        if (!$target.hasClass('active')) {
            $other.each(function(index, self) {
                var $this = $(this);
                    $this.removeClass('active').hide().animate({
                    left: $this.width()
                    }, 500);
            });

            $target.addClass('active').show().css({
                left: -($target.width())
            }).animate({
                left: 0
            }, 500);
        }
    };

    $('a.panel').click(function() {
        $.putMyDesign(this,true);
    });
    
    $('.ajax_thing').click(function(){
            location.hash=$(this).attr('action_type');
            return false
    });
    
    var originalTitle=document.title
    function hashChange(){
        var page=location.hash.slice(1)
        if (page!=""){
            document.title=originalTitle+' - '+ page;
        }

        var $eleObjId=location.hash;
        if(!$eleObjId){
            $eleObjId="#home"
        }
        $.putMyDesign($eleObjId,false);
    }
    
    if ("onhashchange" in window){ // cool browser
        $(window).on('hashchange',hashChange).trigger('hashchange')
    }else{ // lame browser
        var lastHash=''
        setInterval(function(){
            if (lastHash!=location.hash)
                hashChange()
            lastHash=location.hash
        },100)
    }
});

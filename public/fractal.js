  $(document).ready(function(){
    var fractalData = '{"id":1,"region":{"x":0,"y":0,"w":0,"h":0}}';
    var fractalRegion = {"region":{"x":0,"y":0,"w":0,"h":0}}
    var zoom = 1;

    getFractals()
    createFractal(0,0)

    function createFractal(offsetX, offsetY) {
       console.log("create offset " + offsetX + " " + offsetY);
       var img = document.getElementById('MyFractal');
       var width = img.naturalWidth;
       if (width == 0)
            width = 600;
       var height = img.naturalHeight;
       if (height == 0)
            height = 600;

       var percentOffsetX = offsetX/width;
       var percentOffsetY = offsetY/height;
       console.log("create % offset " + percentOffsetX + " " + percentOffsetY);

       console.log("create fractal region " + fractalRegion.w + " " + fractalRegion.h);
       var fractalOffsetX = fractalRegion.w * percentOffsetX;
       var fractalOffsetY = fractalRegion.h * percentOffsetY;
       console.log("create fractal offset " + fractalOffsetX + " " + fractalOffsetY);
       fractalRegion.x += fractalOffsetX
       fractalRegion.y += fractalOffsetY

       if (zoom > 1) {
        fractalRegion.w /= 2
        fractalRegion.h /= 2
        fractalRegion.x += fractalRegion.w/2
        fractalRegion.y += fractalRegion.h/2
       } else if (zoom < 0) {
        fractalRegion.x -= fractalRegion.w/2
        fractalRegion.y -= fractalRegion.h/2
        fractalRegion.w *= 2
        fractalRegion.h *= 2
       }
       console.log("create updated region " + JSON.stringify(fractalRegion));
       console.log("scaled region " + fractalRegion.x + " " + fractalRegion.y + " to " +  (fractalRegion.x+fractalRegion.w) + " " + (fractalRegion.y+fractalRegion.h));

       zoom = 2
      $.ajax({
            type: "POST",
            url: "fractal",
            async: false,
            data: JSON.stringify({region:fractalRegion, "size":{"w":width, "h":height} }),
            contentType: 'application/json',
            dataType: "text",
            success: function(data){
                $("#MyFractal").attr("src", 'data:image/jpg;base64,' + data)
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log("error " + textStatus + " " + errorThrown);
                console.log("incoming Text " + jqXHR.responseText);
            }
        });
    }


    function getFractals() {
       $.ajax({
            type: "GET",
            url: "fractals",
            async: false,
            dataType: "json",
            success: function(data){
                console.log("GET fractals " + JSON.stringify(data));
                fractalData = JSON.stringify(data)
                fractalRegion = data.region
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log("GET fractals error " + textStatus + " " + errorThrown);
                console.log("GET fractals error incoming Text " + jqXHR.responseText);
            }
        });
    }

    $('.reset-button').click(function(){
        zoom = 1
        getFractals()
        createFractal(0,0)
    });

    $('.zoom-out-button').click(function(){
       zoom = -1
       createFractal(0, 0);
    });

    $('.color-button').click(function(){
       $.ajax({
            type: "PUT",
            url: "color",
            async: false,
            dataType: "text",
            success: function(data){
              console.log("changed color " + data);
              },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log("error " + textStatus + " " + errorThrown);
                console.log("incoming Text " + jqXHR.responseText);
            }
        });          zoom = 0
       createFractal(0, 0);
    });

    $('#MyFractal').click(function(e){
        var img = document.getElementById('MyFractal');
        var width = img.naturalWidth;
        var height = img.naturalHeight;
        var offsetX = e.pageX - e.target.offsetLeft - width/2;
        var offsetY = e.pageY - e.target.offsetTop - height/2;
        createFractal(offsetX, offsetY);
    });
  });


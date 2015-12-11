  $(document).ready(function() {
      var fractalData;
      var fractalRegion;
      var colorMaps;
      var zoomMode = 0;
      var zoomLevel = 0;
      var colorSetId = "2";
      var cX=0.0;
      var cY=0.0;

      getFractals();
      getColors();
      setFractal(1)

      renderFractal(0, 0);
      $('#fractal-loading').toggle();
      $('#julia-picker').hide();

      for (var i = 0; i < fractalData.fractals.length; i++) {
           console.log("fractal-change-menu append " + fractalData.fractals[i].id + " " + fractalData.fractals[i].name);
          $('#fractal-change-menu').append('<li value="' + fractalData.fractals[i].id  + '"><a href="#">' + fractalData.fractals[i].name + '</a></li>');
      }


      function renderFractal(offsetX, offsetY) {
          $('#fractal-loading').toggle();
          $("#fractal-image").attr("style", 'border: 1px solid #00dd00; cursor:crosshair');

          console.log("create offset " + offsetX + " " + offsetY);
          var img = document.getElementById('fractal-image');
          var width = img.naturalWidth;
          if (width == 0)
              width = 600;
          var height = img.naturalHeight;
          if (height == 0)
              height = 600;

          var percentOffsetX = offsetX / width;
          var percentOffsetY = offsetY / height;
          console.log("create % offset " + percentOffsetX + " " + percentOffsetY);

          console.log("create fractal region " + fractalRegion.w + " " + fractalRegion.h);
          var fractalOffsetX = fractalRegion.w * percentOffsetX;
          var fractalOffsetY = fractalRegion.h * percentOffsetY;
          console.log("create fractal offset " + fractalOffsetX + " " + fractalOffsetY);
          fractalRegion.x += fractalOffsetX
          fractalRegion.y += fractalOffsetY

          if (zoomMode > 0) {
              fractalRegion.w /= 2
              fractalRegion.h /= 2
              fractalRegion.x += fractalRegion.w / 2
              fractalRegion.y += fractalRegion.h / 2
          } else if (zoomMode < 0) {
              fractalRegion.x -= fractalRegion.w / 2
              fractalRegion.y -= fractalRegion.h / 2
              fractalRegion.w *= 2
              fractalRegion.h *= 2
          }
          console.log("create updated region " + JSON.stringify(fractalRegion));
          console.log("scaled region " + fractalRegion.x + " " + fractalRegion.y + " to " + (fractalRegion.x + fractalRegion.w) + " " + (fractalRegion.y + fractalRegion.h));

           $.ajax({
              type: "POST",
              url: "fractal",
              async: false,
              data: JSON.stringify({
                  "id":selectedFractal.id,
                  "colorId": colorSetId,
                  "region": fractalRegion,
                  "size": {
                      "w": width,
                      "h": height
                  },
                  "julia": {
                      "x": cX,
                      "y": cY
                  }
              }),
              contentType: 'application/json',
              dataType: "text",
              success: function(data) {
                  $("#fractal-image").attr("src", 'data:image/jpg;base64,' + data);
                  $('#fractal-loading').toggle();
                  $("#fractal-image").attr("style", 'cursor:crosshair');
              },
              error: function(jqXHR, textStatus, errorThrown) {
                  console.log("error " + textStatus + " " + errorThrown);
                  console.log("incoming Text " + jqXHR.responseText);
                  $('#fractal-loading').toggle();
                  $("#fractal-image").attr("style", 'cursor:crosshair');

              }
          });
      }

      function renderJuliaNavFractal(offsetX, offsetY) {
      console.log("renderJuliaNavFractal "+selectedFractal.parentId )
          if (selectedFractal.parentId == 0){
            $('#julia-picker').hide();
            return;
          }
          $('#julia-picker').show();
          console.log("renderJuliaNavFractal offset " + offsetX + " " + offsetY);
          var img = document.getElementById('julia-picker-image');
          var width = img.naturalWidth;
          if (width == 0)
              width = 200;
          var height = img.naturalHeight;
          if (height == 0)
              height = 200;

          var region = JSON.parse(getFractal(selectedFractal.parentId).region);
          region.x += .25
          region.w -= .5
           region.y += .25
           region.h -= .5
          $.ajax({
              type: "POST",
              url: "fractal",
              async: false,
              data: JSON.stringify({
                 "id":selectedFractal.parentId,
                  "colorId": "4",
                  "region": region,
                  "size": {
                      "w": width,
                      "h": height
                  },
                  "julia": {
                      "x": 0,
                      "y": 0
                  }
              }),
              contentType: 'application/json',
              dataType: "text",
              success: function(data) {
                  $("#julia-picker-image").attr("src", 'data:image/jpg;base64,' + data)
              },
              error: function(jqXHR, textStatus, errorThrown) {
                  console.log("error " + textStatus + " " + errorThrown);
                  console.log("incoming Text " + jqXHR.responseText);
              }
          });
      }

      function setFractal(fractalId) {
       for (var i = 0; i < fractalData.fractals.length; i++) {
        if (fractalData.fractals[i].id == fractalId) {
            $('#fractal-name').html('<a href="#"><span class="label label-primary">'+fractalData.fractals[i].name+'</span></a>')
            selectedFractal = fractalData.fractals[i]
            fractalRegion = JSON.parse(fractalData.fractals[i].region);
         }
        }
      }

    function getFractal(fractalId) {
     for (var i = 0; i < fractalData.fractals.length; i++) {
      if (fractalData.fractals[i].id == fractalId) {
          return fractalData.fractals[i]
       }
      }
    }

      function getFractals() {
          $.ajax({
              type: "GET",
              url: "fractals",
              async: false,
              dataType: "json",
              success: function(data) {
                  console.log("GET fractals " + JSON.stringify(data));
                  fractalData = data
              },
              error: function(jqXHR, textStatus, errorThrown) {
                  console.log("GET fractals error " + textStatus + " " + errorThrown);
                  console.log("GET fractals error incoming Text " + jqXHR.responseText);
              }
          });
      }

      function getColors() {
          $.ajax({
              type: "GET",
              url: "colors",
              async: false,
              dataType: "json",
              success: function(data) {
                  colorMaps = data
                  console.log("GET colors " + JSON.stringify(data));
              },
              error: function(jqXHR, textStatus, errorThrown) {
                  console.log("GET colors error " + textStatus + " " + errorThrown);
                  console.log("GET colors error incoming Text " + jqXHR.responseText);
              }
          });
      }

      function reset() {
         zoomMode = 0;
         zoomLevel = 0;
         $('#zoom-badge').html(zoomLevel);
         setFractal(selectedFractal.id);
      }

      $('#reset-button').click(function() {
         reset()
         renderFractal(0, 0);
      });

      $('#zoom-out-button').click(function() {
         zoomLevel -= 1;
         $('#zoom-badge').html(zoomLevel);
         zoomMode = -1;
         renderFractal(0, 0);
      });

      $('#zoom-in-button').click(function() {
         zoomLevel += 1;
         $('#zoom-badge').html(zoomLevel)
         zoomMode = 1
         renderFractal(0, 0);
      });

      function clickColor() {
          console.log("clickColor ");
          var $this = $(this);
          colorSetId = $this.attr("value");
          console.log("selKeyVal " + colorSetId);
         $.ajax({
              type: "PUT",
              url: "color",
              async: false,
              data: JSON.stringify({"id": colorSetId}),
              contentType: 'application/json',
              dataType: "text",
              success: function(data) {
                  console.log("changed color " + data);
              },
              error: function(jqXHR, textStatus, errorThrown) {
                  console.log("error " + textStatus + " " + errorThrown);
                  console.log("incoming Text " + jqXHR.responseText);
              }
          });
          zoomMode = 0
          renderFractal(0, 0);
      }


      $('#fractal-change-menu li').click(function(e) {
          console.log("fractal-change-menu " + $(this).attr("value"));
          reset();
          setFractal($(this).attr("value"));
          cX = -0.8;
          cY = -0.2249;
          renderFractal(0,0);
          renderJuliaNavFractal(0,0);
     });


      $('#julia-picker-image').click(function(e) {
          var img = document.getElementById('julia-picker-image');
          var offsetX = e.pageX - e.target.offsetLeft - e.target.offsetParent.offsetLeft - img.naturalWidth / 2;
          var offsetY = e.pageY - e.target.offsetTop- e.target.offsetParent.offsetTop - img.naturalHeight / 2;


          var percentOffsetX = offsetX / img.naturalWidth;
          var percentOffsetY = offsetY / img.naturalHeight;
          var region = JSON.parse(fractalData.fractals[0].region)

          var x = region.x + region.w/2 + region.w*percentOffsetX
          var y = -(region.y + region.h/2 + region.h*percentOffsetY)

          console.log("click julia-picker-image" + x + " " + y);
          reset();
          cX = x;
          cY = y;
          renderFractal(0, 0);

      });

      $('#fractal-image').click(function(e) {
          var img = document.getElementById('fractal-image');
          var offsetX = e.pageX - e.target.offsetLeft - e.target.offsetParent.offsetLeft - img.naturalWidth / 2;
          var offsetY = e.pageY - e.target.offsetTop- e.target.offsetParent.offsetTop - img.naturalHeight / 2;

          zoomMode = 1;
          zoomLevel += 1;
          $('#zoom-badge').html(zoomLevel)
          renderJuliaNavFractal(0,0)
          renderFractal(offsetX, offsetY);
      });

      for (var i = 0; i < colorMaps.colors.length; i++) {
          var button='<button type="button" class="btn btn-default" value="' + colorMaps.colors[i].id + '" id="color-button'+colorMaps.colors[i].id +'">' + colorMaps.colors[i].name + '</button>';
          $("#color-buttons").append(button)
          var id = "color-button"+colorMaps.colors[i].id;
          $("#"+id).on("click", clickColor);
      }
  });
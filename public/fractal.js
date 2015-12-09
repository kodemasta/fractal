  $(document).ready(function() {
      var fractalData;
      var fractalRegion;
      var colorMaps;
      var zoomMode = 0;
      var zoomLevel = 0;
      var colorSetId = "2";

      getFractals();
      getColors();
      createFractal(0, 0)
      createJuliaNavFractal(0,0);

      for (var i = 0; i < fractalData.fractals.length; i++) {
           console.log("fractal-change-menu append " + fractalData.fractals[i].id + " " + fractalData.fractals[i].name);
          $('#fractal-change-menu').append('<li value="' + fractalData.fractals[i].id  + '"><a href="#">' + fractalData.fractals[i].name + '</a></li>');
      }

      $('#julia-picker').hide();

      function createFractal(offsetX, offsetY) {
          $('#fractal-loading').show();
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
                  }
              }),
              contentType: 'application/json',
              dataType: "text",
              success: function(data) {
                  $("#fractal-image").attr("src", 'data:image/jpg;base64,' + data);
                  $('#fractal-loading').hide();
                  $("#fractal-image").attr("style", 'cursor:crosshair');
              },
              error: function(jqXHR, textStatus, errorThrown) {
                  console.log("error " + textStatus + " " + errorThrown);
                  console.log("incoming Text " + jqXHR.responseText);
                  $('#fractal-loading').hide();
                  $("#fractal-image").attr("style", 'cursor:crosshair');

              }
          });
      }

      function createJuliaNavFractal(offsetX, offsetY) {
          console.log("create offset " + offsetX + " " + offsetY);
          var img = document.getElementById('fractal-image2');
          var width = img.naturalWidth;
          if (width == 0)
              width = 200;
          var height = img.naturalHeight;
          if (height == 0)
              height = 200;

           $.ajax({
              type: "POST",
              url: "fractal",
              async: false,
              data: JSON.stringify({
                  "id":selectedFractal.id,
                  "colorId": colorSetId,
                  "region":  JSON.parse(selectedFractal.region),
                  "size": {
                      "w": width,
                      "h": height
                  }
              }),
              contentType: 'application/json',
              dataType: "text",
              success: function(data) {
                  $("#fractal-image2").attr("src", 'data:image/jpg;base64,' + data)
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

      function getFractals() {
          $.ajax({
              type: "GET",
              url: "fractals",
              async: false,
              dataType: "json",
              success: function(data) {
                  console.log("GET fractals " + JSON.stringify(data));
                  fractalData = data
                  setFractal(0)
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

      $('#reset-button').click(function() {
         zoomMode = 0;
         zoomLevel = 0;
         $('#zoom-badge').html(zoomLevel);
         setFractal(selectedFractal.id);
         createFractal(0, 0);
      });

      $('#zoom-out-button').click(function() {
         zoomLevel -= 1;
         $('#zoom-badge').html(zoomLevel);
         zoomMode = -1;
         createFractal(0, 0);
      });

      $('#zoom-in-button').click(function() {
         zoomLevel += 1;
         $('#zoom-badge').html(zoomLevel)
         zoomMode = 1
         createFractal(0, 0);
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
          createFractal(0, 0);
      }

        function clickFractal() {
            console.log("clickFractal");
            var $this = $(this);
            fractalId = $this.attr("value");
            console.log("selKeyVal " + fractalId);

            setFractal(fractalId)

            zoomMode = 0
            zoomLevel = 0;
            $('#zoom-badge').html(zoomLevel)
            createFractal(0, 0);
        }


      $('#fractal-change-menu li').click(clickFractal);


      $('#fractal-image').click(function(e) {
          var img = document.getElementById('fractal-image');
          var width = img.naturalWidth;
          var height = img.naturalHeight;
          var offsetX = e.pageX - e.target.offsetLeft - e.target.offsetParent.offsetLeft - width / 2;
          var offsetY = e.pageY - e.target.offsetTop- e.target.offsetParent.offsetTop - height / 2;

          zoomMode = 1;
          zoomLevel += 1;
          $('#zoom-badge').html(zoomLevel)
          createFractal(offsetX, offsetY);
      });

       for (var i = 0; i < colorMaps.colors.length; i++) {
          var button='<button type="button" class="btn btn-default" value="' + colorMaps.colors[i].id + '" id="color-button'+colorMaps.colors[i].id +'">' + colorMaps.colors[i].name + '</button>';
          $("#color-buttons").append(button)
          var id = "color-button"+colorMaps.colors[i].id;
          $("#"+id).on("click", clickColor);

    }
  });
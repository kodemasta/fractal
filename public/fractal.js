  $(document).ready(function() {
      var fractalData = '{"id":1,"region":{"x":0,"y":0,"w":0,"h":0}}';
      var fractalRegion = {
          "region": {
              "x": 0,
              "y": 0,
              "w": 0,
              "h": 0
          }
      }
      var colorMaps
      var zoomMode = 0;
      var zoomLevel = 0;
      var colorSetId = "2";

      getFractals();
      getColors();
      createFractal(0, 0)

      for (var i = 0; i < colorMaps.colors.length; i++) {
          $('#color-change-menu').append('<li value="' + colorMaps.colors[i].id  + '"><a href="#">' + colorMaps.colors[i].name + '</a></li>');
      }


      function createFractal(offsetX, offsetY) {
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
                  "id": colorSetId,
                  "region": fractalRegion,
                  "size": {
                      "w": width,
                      "h": height
                  }
              }),
              contentType: 'application/json',
              dataType: "text",
              success: function(data) {
                  $("#fractal-image").attr("src", 'data:image/jpg;base64,' + data)
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
              success: function(data) {
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
         $('#zoom-badge').html(zoomLevel)
        getFractals();
         createFractal(0, 0)
      });

      $('#zoom-out-button').click(function() {
         zoomLevel -= 1;
         $('#zoom-badge').html(zoomLevel)
         zoomMode = -1
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

      $('#color-change-menu li').click(clickColor);

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
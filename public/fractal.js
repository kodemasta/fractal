$(document).ready(function() {
    var selectedFractal;
    var selectedColor;
    var selectedEquation;
    var fractalData;
    var currentFractalRegion = null;
    var previewFractalRegion = null;
    var colorData;
    var equationData;
    var zoomMode = 0;
    var zoomLevel = 0;
    var cX = 0.0;
    var cY = 0.0;
    var computing = false;
    var computingJulia = false;
    var selectedSize = "512";
    var initFractalType = 1;
    $("#size-select").val(selectedSize).change();
    init();


    function init() {
        getEquations(initFractalType);
        getFractals();
        getColors();

        $("select#size-select").change(function(){
           clickSize($(this).children(":selected").val())
        });

        resetZoomButtonClicked();
        $('#fractal-loading').toggle();
        $('#julia-picker').hide();
        renderFractal(0, 0);
    }

    function toggleCompute(toggle) {
        var img = document.getElementById('fractal-image');
        var pickerImage = document.getElementById('julia-picker-image');
        if (toggle) {
            computing = true;
            jQuery('#fractal-loading').show();
            $(img).attr("style", 'border: 2px solid #00AA00;cursor:wait;');
            $(img).prop("disabled", true);
            $(pickerImage).attr("style", 'cursor:wait;');
            $(pickerImage).prop("disabled", true);
            $("#size-select").prop("disabled", true);
            $("#fractal-select").prop("disabled", true);
            $("#color-select").prop("disabled", true);
            $('.input-number').prop("disabled", true);
            $("#reset-button2").prop("disabled", true);
            $("#equation-select").prop("disabled", true);
      } else {
            computing = false;
            jQuery('#fractal-loading').hide();
            $(img).attr("style", 'cursor:crosshair;');
            $(img).prop("disabled", false);
            $(pickerImage).attr("style", 'cursor:crosshair;');
            $(pickerImage).prop("disabled", false);
            $("#size-select").prop("disabled", false);
            $("#color-select").prop("disabled", false);
            $("#fractal-select").prop("disabled", false);
            $('.input-number').prop("disabled", false);
            $("#reset-button2").prop("disabled", false);
            $("#equation-select").prop("disabled", false);
       }
    }

    function toggleJuliaPreviewCompute(toggle) {
        var fractalImage = document.getElementById('fractal-image');
        var previewImage = document.getElementById('julia-preview-image');
        var pickerImage = document.getElementById('julia-picker-image');
        if (toggle) {
            computingJulia = true;
            jQuery('#julia-fractal-loading').show();
            //$(fractalImage).attr("style", 'border: 2px solid #00AA00;');
            $(previewImage).attr("style", 'border: 2px solid #00AA00;');
            //$(pickerImage).attr("style", 'border: 2px solid #00AA00;');

            $(fractalImage).prop("disabled", true);
            $(previewImage).prop("disabled", true);
            $(pickerImage).prop("disabled", true);
       } else {
            computingJulia = false;
            jQuery('#julia-fractal-loading').hide();
            //$(fractalImage).attr("style", 'cursor:crosshair;');
            $(previewImage).attr("style", '');
            //$(pickerImage).attr("style", 'cursor:crosshair');

            //$(fractalImage).prop("disabled", false);
            $(previewImage).prop("disabled", false);
            //$(pickerImage).prop("disabled", false);
        }
    }

    function renderFractal(offsetX, offsetY) {
        if (computing)
            return;
        toggleCompute(true)
        console.log("renderFractal offset " + offsetX + " " + offsetY);
        var img = document.getElementById('fractal-image');
        width = Number(selectedSize);
        height = width;
        var percentOffsetX = offsetX / width;
        var percentOffsetY = offsetY / height;
        console.log("create % offset " + percentOffsetX + " " + percentOffsetY);
        if (currentFractalRegion != null)
        {
            //console.log("create fractal region " + currentFractalRegion.w + " " + currentFractalRegion.h);
            var fractalOffsetX = currentFractalRegion.w * percentOffsetX;
            var fractalOffsetY = currentFractalRegion.h * percentOffsetY;
            console.log("create fractal offset " + fractalOffsetX + " " + fractalOffsetY);
            currentFractalRegion.x += fractalOffsetX
            currentFractalRegion.y += fractalOffsetY
            if (zoomMode > 0) {
                currentFractalRegion.w /= 2
                currentFractalRegion.h /= 2
                currentFractalRegion.x += currentFractalRegion.w / 2
                currentFractalRegion.y += currentFractalRegion.h / 2
            } else if (zoomMode < 0) {
                currentFractalRegion.x -= currentFractalRegion.w / 2
                currentFractalRegion.y -= currentFractalRegion.h / 2
                currentFractalRegion.w *= 2
                currentFractalRegion.h *= 2
            }
            console.log("create updated region " + JSON.stringify(currentFractalRegion));
            console.log("scaled region " + currentFractalRegion.x + " " + currentFractalRegion.y + " to " + (currentFractalRegion.x + currentFractalRegion.w) + " " + (currentFractalRegion.y + currentFractalRegion.h));
        }
        $.ajax({
            type: "POST",
            url: "fractal",
            async: true,
            data: JSON.stringify({
                "id": selectedFractal.id,
                "colorId":selectedColor.id,
                "region": currentFractalRegion,
                "equationId": selectedEquation.id,
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

                console.log("POST fractal success");
                toggleCompute(false);
               $("#fractal-image").attr("src", 'data:image/jpg;base64,' + JSON.parse(data).image);
               if (currentFractalRegion == null) {
                 currentFractalRegion = JSON.parse(JSON.parse(data).region)
               }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log("error " + textStatus + " " + errorThrown);
                console.log("incoming Text " + jqXHR.responseText);
                toggleCompute(false);
            }
        });
    }

    function renderJuliaPreviewFractal() {
        if (computingJulia) return;
        if (selectedFractal.id != 2) {
            return;
        }
        toggleJuliaPreviewCompute(true)
        console.log("renderJuliaPreviewFractal");
        var img = document.getElementById('julia-preview-image');
        var width = img.naturalWidth;
        if (width == 0) width = 256;
        var height = img.naturalHeight;
        if (height == 0) height = 256;
        $.ajax({
            type: "POST",
            url: "fractal",
            async: true,
            data: JSON.stringify({
                "id": selectedFractal.id,
                "equationId": selectedEquation.id,
                "colorId": selectedColor.id,
                "region": null,
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
                console.log("POST fractal success");
                toggleJuliaPreviewCompute(false);
                $("#julia-preview-image").attr("src", 'data:image/jpg;base64,' + JSON.parse(data).image);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log("error " + textStatus + " " + errorThrown);
                console.log("incoming Text " + jqXHR.responseText);
                toggleJuliaPreviewCompute(false);
            }
        });
    }

    function renderJuliaNavFractal(offsetX, offsetY) {
        console.log("renderJuliaNavFractal " + selectedFractal.id)
        if (selectedFractal.id !=2) {
            $('#julia-picker').hide();
            return;
        }
        $('#julia-picker').show();
        console.log("renderJuliaNavFractal offset " + offsetX + " " + offsetY);
        var img = document.getElementById('julia-picker-image');
        var width = img.naturalWidth;
        if (width == 0) width = 256;
        var height = img.naturalHeight;
        if (height == 0) height = 256;
//        var region = JSON.parse(getFractal(selectedFractal.id).region);
//        region.x += .25
//        region.w -= .5
//        region.y += .25
//        region.h -= .5
        $.ajax({
            type: "POST",
            url: "fractal",
            async: false,
            data: JSON.stringify({
                "id": "1",
                "equationId": selectedEquation.id,
                "colorId": "6",
                "region": null,
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
                console.log("POST fractal success");
                $("#julia-picker-image").attr("src", 'data:image/jpg;base64,' + JSON.parse(data).image)
                previewFractalRegion = JSON.parse(JSON.parse(data).region);
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
                $('#fractal-name').html('<a href="#"><span class="label label-primary">' + fractalData.fractals[i].name + '</span></a>')
                selectedFractal = fractalData.fractals[i]

                currentFractalRegion = null;
            }
        }
    }

    function setEquation(equationId) {
        for (var i = 0; i < equationData.equations.length; i++) {
            if (equationData.equations[i].id == equationId) {
                selectedEquation = equationData.equations[i];
            }
        }
    }

    function setColor(colorId) {
        for (var i = 0; i < colorData.colors.length; i++) {
            if (colorData.colors[i].id == colorId) {
                console.log("setColor " + colorId)
                selectedColor = colorData.colors[i];
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

    function getColor(colorId) {
        for (var i = 0; i < colorData.colors.length; i++) {
            if (colorData.colors[i].id == colorId) {
                return colorData.colors[i]
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
                $("#fractal-select").empty();
                for (var i = 0; i < fractalData.fractals.length; i++) {
                    console.log("fractal-select append " + fractalData.fractals[i].id + " " + fractalData.fractals[i].name);
                    var option = '<option value="' + fractalData.fractals[i].id + '" id="fractal-select' + fractalData.fractals[i].id + '">' + fractalData.fractals[i].name + '</option>';
                    $("#fractal-select").append(option);
                }

                $("select#fractal-select").change(function(){
                   clickFractalType($(this).children(":selected").val())
                });

                setFractal(initFractalType)

            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log("GET fractals error " + textStatus + " " + errorThrown);
                console.log("GET fractals error incoming Text " + jqXHR.responseText);
            }
        });
    }

    function getEquations(fractalId) {
        $.ajax({
            type: "GET",
            url: "equations?"+"id="+fractalId,
            async: false,
            dataType: "json",
            success: function(data) {
                console.log("GET equations " + JSON.stringify(data));
                $("#equation-select").empty();
                equationData = data;
                for (var i = 0; i < equationData.equations.length; i++) {
                    console.log("equation-select append " + equationData.equations[i].id + " " + equationData.equations[i].name);
                    var option = '<option value="' + equationData.equations[i].id + '" id="equation-select' + equationData.equations[i].id + '">' + equationData.equations[i].name + '</option>';
                    $("#equation-select").append(option);
                }
                $("select#equation-select").change(function(){
                   clickEquationType($(this).children(":selected").val())
                });

                setEquation(equationData.equations[0].id) //default
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log("GET equations error " + textStatus + " " + errorThrown);
                console.log("GET equations error incoming Text " + jqXHR.responseText);
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
                colorData = data
                console.log("GET colors " + JSON.stringify(data));
                for (var i = 0; i < colorData.colors.length; i++) {
                    console.log("color-select append " + colorData.colors[i].id + " " + colorData.colors[i].name);
                    var option = '<option value="' + colorData.colors[i].id + '" id="color-select' + colorData.colors[i].id + '">' + colorData.colors[i].name + '</option>';
                    $("#color-select").append(option);
                }

                $("select#color-select").change(function(){
                   clickColor($(this).children(":selected").val())
                });
                setColor(2)
                //var colorOption = document.getElementById('color-select');
                $("#color-select").val(2);
                $("#color-select").change();

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
        currentFractalRegion = null;
        $('#zoom-badge').html(zoomLevel);
        setFractal(selectedFractal.id);
    }
    $('#reset-button').click(function() {
        reset()
        renderFractal(0, 0);
    });
    $('#reset-button2').click(function() {
        reset()
        resetZoomButtonClicked();
        renderFractal(0, 0);
    });

    function zoomOut() {
        zoomLevel -= 1;
        $('#zoom-badge').html(zoomLevel);
        zoomMode = -1;
    }

    function zoomIn() {
        zoomLevel += 1;
        $('#zoom-badge').html(zoomLevel)
        zoomMode = 1
    }
    $('#zoom-out-button').click(function() {
        zoomOut();
        renderFractal(0, 0);
    });
    $('#zoom-in-button').click(function() {
        zoomIn();
        renderFractal(0, 0);
    });

    function clickColor() {

        var $this = $(this);
        id = $this.attr("value");
        console.log("clickColor1 " + id);
        if (selectedColor.id == id) return;
        zoomMode = 0;
        setColor(id);
        renderJuliaPreviewFractal();
        renderFractal(0, 0);
    }

    function clickColor(id) {

        console.log("clickColor2 " + id);
        if (selectedColor.id == id) return;
        zoomMode = 0;
        setColor(id);
        renderJuliaPreviewFractal();
        renderFractal(0, 0);
    }

    function clickSize(size) {
        if (computing || computingJulia)
            return;
        console.log("clickSize " + size);
        if (selectedSize == size) return;
        selectedSize = size;
        $("#fractal-image").attr("WIDTH", selectedSize);
        $("#fractal-image").attr("HEIGHT", selectedSize);
        zoomMode = 0;
        renderFractal(0, 0);
        }
        /*   $('#fractal-change-menu li').click(function(e) {
          console.log("fractal-change-menu " + $(this).attr("value"));
          reset();
          setFractal($(this).attr("value"));
          cX = -0.8;
          cY = -0.2249;
          resetZoomButtonClicked();
          renderFractal(0,0);
          renderJuliaNavFractal(0,0);
     });*/

//    function clickFractalType() {
//        var $this = $(this);
//        type = $this.attr("value");
//        console.log("clickFractalType " + type);
//        if (selectedFractal.id == type) return;
//        reset();
//        setFractal(type);
//        cX = -0.8;
//        cY = -0.2249;
//        resetZoomButtonClicked();
//        renderFractal(0, 0);
//        renderJuliaPreviewFractal();
//        renderJuliaNavFractal(0, 0);
//    }

    function clickEquationType(type) {
        console.log("clickEquationType " + type);
        if (selectedEquation.id == type) return;
        reset();
        setEquation(type);
        cX = -0.8;
        cY = -0.2249;
        resetZoomButtonClicked();
        renderFractal(0, 0);
        renderJuliaPreviewFractal();
        renderJuliaNavFractal(0, 0);
    }


    function clickFractalType(type) {
        console.log("clickFractalType " + type);
        if (selectedFractal.id == type) return;
        reset();
        getEquations(type);
        setEquation(1)
        setFractal(type);
        cX = -0.8;
        cY = -0.2249;
        resetZoomButtonClicked();
        renderFractal(0, 0);
        renderJuliaPreviewFractal();
        renderJuliaNavFractal(0, 0);
    }

    $("#size-select").click(function(e) {
        console.log("size-select " + $(this).attr("value"));
    });

    $('#julia-picker-image').click(function(e) {
        if (computing || computingJulia)
            return;
        var img = document.getElementById('julia-picker-image');
        var offsetX = e.pageX - e.target.offsetLeft - e.target.offsetParent.offsetLeft - img.naturalWidth / 2;
        var offsetY = e.pageY - e.target.offsetTop - e.target.offsetParent.offsetTop - img.naturalHeight / 2;
        var percentOffsetX = offsetX / img.naturalWidth;
        var percentOffsetY = offsetY / img.naturalHeight;
        var region = previewFractalRegion;
        /*region.x += .25
        region.w -= .5
        region.y += .25
        region.h -= .5*/
        var x = region.x + region.w / 2 + region.w * percentOffsetX
        var y = -(region.y + region.h / 2 + region.h * percentOffsetY)
        console.log("click julia-picker-image " + x + " " + y);
        reset();
        cX = x;
        cY = y;
        renderJuliaPreviewFractal();
        renderFractal(0, 0);
    });

    $('#fractal-image').click(function(e) {
       if (computing || computingJulia)
            return;
        var img = document.getElementById('fractal-image');
        var offsetX = e.pageX - e.target.offsetLeft - e.target.offsetParent.offsetLeft - selectedSize / 2;
        var offsetY = e.pageY - e.target.offsetTop - e.target.offsetParent.offsetTop - selectedSize / 2;
        zoomMode = 1;
        zoomLevel += 1;
        $('#zoom-badge').html(zoomLevel)
        var buttonPlus = document.getElementById('plus-button');
        zoomButtonClicked($(buttonPlus));
        renderJuliaNavFractal(0, 0)
        renderFractal(offsetX, offsetY);
    });

    function resetZoomButtonClicked() {
        if (computing || computingJulia)
            return;
        var buttonPlus = document.getElementById('plus-button');
        fieldName = $(buttonPlus).attr('data-field');
        type = $(buttonPlus).attr('data-type');
        var input = $('#' + fieldName);
        input.val(0).change();
    }

    function zoomButtonClicked(button) {
        if (computing || computingJulia)
         return;
       fieldName = button.attr('data-field');
        type = button.attr('data-type');
        var input = $('#' + fieldName);
        var currentVal = parseInt(input.val(), 10);
        console.log('zoom clicked ' + currentVal);
        if (!isNaN(currentVal)) {
            if (type == 'minus') {
                if (currentVal > input.attr('min')) {
                    input.val(currentVal - 1).change();
                    zoomOut();
                    return true;
                }
                if (currentVal == input.attr('min')) {
                    $(this).attr('disabled', true);
                    console.log('minus clicked == min');
                }
            } else if (type == 'plus') {
                if (currentVal < input.attr('max')) {
                    input.val(currentVal + 1).change();
                    zoomIn();
                    return true;
                }
                if (currentVal == input.attr('max')) {
                    $(this).attr('disabled', true);
                    console.log('plus clicked == max');
                }
            }
        } else {
            input.val(0);
        }
        return false;
    }

    $('#about-icon').click(function(e){
       console.log('about-icon clicked');
        $("#myModal").modal('show');
    });

        // ---------------------------
        // manage counters on bulk tab
    $('.btn-number').click(function(e) {
        console.log('btn-number clicked');
        e.preventDefault();
        if (zoomButtonClicked($(this))) renderFractal(0, 0);
    });
    // delete input value if it's 0
    $('.input-number').focusin(function() {
        $(this).data('oldValue', $(this).val());
        if ($(this).val() == 0) {
            $(this).val('');
        }
    });
    // if the user didn't enter a number, set it back to 0
    $('.input-number').focusout(function() {
        if ($(this).val() == '') {
            $(this).val(0);
        }
    });
    // handle min/max values on inputs
    $('.input-number').change(function() {
        if (computing || computingJulia)
            return;
        console.log('input-number clicked');
        minValue = parseInt($(this).attr('min'), 10);
        maxValue = parseInt($(this).attr('max'), 10);
        valueCurrent = parseInt($(this).val(), 10);
        name = $(this).attr('id');
        if (valueCurrent >= minValue) {
            $(".btn-number[data-type='minus'][data-field='" + name + "']").removeAttr('disabled');
        } else {
            console.log('Sorry, the minimum value was reached');
            $(this).val($(this).data('oldValue'));
            $(".btn-number[data-type='minus'][data-field='" + name + "']").addAttr('disabled');
        }
        if (valueCurrent <= maxValue) {
            $(".btn-number[data-type='plus'][data-field='" + name + "']").removeAttr('disabled');
        } else {
            console.log('Sorry, the maximum value was reached');
            $(".btn-number[data-type='plus'][data-field='" + name + "']").addAttr('disabled');
            $(this).val($(this).data('oldValue'));
        }
    });


    $("#ok-button").on("click", function(e) {
        console.log("button pressed"); // just as an example...
        $("#myModal").modal('hide'); // dismiss the dialog
     });


    $("#myModal").on("hide", function() { // remove the event listeners when the dialog is dismissed
        $("#myModal a.btn").off("click");
    });
    $("#myModal").on("hidden", function() { // remove the actual elements from the DOM when fully hidden
        $("#myModal").remove();
    });

    $("#myModal").modal({ // wire up the actual modal functionality and show the dialog
        "backdrop" : "static",
        "keyboard" : true,
        "show" : false // ensure the modal is shown immediately
    });
});
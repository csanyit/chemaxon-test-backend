var marvin = null;
var currentElement = null;

function initializeMarvin() {
    ChemicalizeMarvinJs.createEditor("#marvin-display-div").then( (marvinResult) => {
        marvinResult.setDisplaySettings({
            toolbars: "view3d",
            displayMode: "BALLSTICK",
            defaultTool: "rotate3d",
            implicitHydrogen: "OFF",
            chiralFlagVisible: false,
            valenceErrorVisible: false,
            disableContextMenu: true,
        });
        marvin = marvinResult;
    });
}

function initializePage() {
    $('#summary_table_head').hide();
    $('#marvin-display-div').hide();
    $('#searchButton').prop('disabled', true);
    $('#chemicalname').on('input',(e) => {
        if ($('#chemicalname').val() == '') {
            $('#searchButton').prop('disabled', true);
        } else {
            $('#searchButton').prop('disabled', false);
        }
    });
}

function searctChemicalDetails(chemicalName) {
    if ( chemicalName != currentElement ) {
      currentElement = chemicalName;
      $.getJSON("/api/v1/description/summary?name="+chemicalName, result => {
        showChemicalDetails(result, chemicalName);
      }).fail((err) => {
         console.error(err);
         handleRESTErrorResponse(err);
      });
    }
}

function showChemicalDetails(detailsResponseArray, chemicalName) {
    if ( detailsResponseArray instanceof Array ) {
      $('#summary_table_head').show();
      $('#summary_table_chemical_name').html(chemicalName);
      $('#summary_table_body').html(detailsResponseArray.map( element => {
        return '<tr><td>'+element.label+'</td><td>'+element.value.toString()+'</td></tr>';
      }));
      marvin.importStructure3d();
      marvin.importStructure3d("name", chemicalName);
      $('#marvin-display-div').show();
    }
}

function handleRESTErrorResponse(err) {
    $('#summary_table_head').hide();
    $('#marvin-display-div').hide();
    $('#summary_table_body').html(
        '<tr><td><div class="alert alert-danger" role="alert">Error during querying Chemicalize Pro (status '+err.responseJSON.status+'): '+JSON.parse(err.responseJSON.message).message+'</div></td></tr>'
    );
}
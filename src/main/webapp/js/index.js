/**
 * Created by liurong on 2019/2/13.
 */
console.log(document.getElementsByTagName('body'))
var ServerUrl = 'http://182.151.22.247:8089';
var selectedId, discountNum, xmmc, file;
$(function () {
    getAllProject();
    $('#rate').on('input propertychange', function (e) {
        var value = e.target.value;
        console.log(value)
        if( value == '') {
            value = 0;
        }
        if(value == 0) {
            $('#price').text(0);
        }
        discountNum = parseFloat(value);
        if(discountNum && selectedId) {
            getTotalNum();
            getExportFile();
        }
    })
    $('#newFile').on('change', function (e) {
        let name = $('#newName')[0].value;
        if(e.target.files.length) {
            xmmc = name;
            file = e.target.files[0];
            importProject();
        }
    })
    $('#project-table').on('click', 'input', function(e) {
        // e.preventDefult();
        var id = parseInt($(this).attr('data-index'));
        $.get(ServerUrl + '/project/del?id=' + id, function (data) {
            getAllProject();
            // console.log(data);
        })
        // console.log(e.target.getAttribute("data-index"))
    })
});
function getTotalNum() {
    console.log(selectedId, discountNum);
    $.get(ServerUrl + '/conbersiontool/discount?xmbh=' + selectedId + '&discount=' + discountNum, function (data) {
        let price = data.data;
        $('#price').text(price);
    })
}
function importProject() {
    let data = new FormData();
    data.append('xmmc', xmmc);
    data.append('file', file);
    $.ajax({
        url: ServerUrl + '/conbersiontool/importExcel',
        type: 'POST',
        data: data,
        contentType: false,
        processData: false,
        success: function (data) {
            getAllProject();
            alert(data.data)
            $('#newName').val('');
            var obj = document.getElementById('newFile');
            obj.outerHTML=obj.outerHTML;
        },
        error: function () {
            alert("请求数据异常！");
        }
    });
}
function getExportFile() {
    console.log(selectedId, discountNum);
    $('#result-href').attr("href",ServerUrl + '/conbersiontool/exportExcel?xmbh=' + selectedId + '&discount=' + discountNum);
}
function projectChange(){
    var objS = document.getElementById("selectGroup");
    var projectId = objS.options[objS.selectedIndex].value;
    selectedId = projectId;
    if(discountNum && selectedId) {
        getTotalNum();
        getExportFile();
    }
}
function getAllProject() {
    $.get(ServerUrl + '/project/findAll', function (data) {
        var projects = data.data;
        var selectGroupHtml = '';
        var projectTableHtml = '';
        if(projects && projects.length) {
            projects.forEach(function (item) {
                selectGroupHtml += '<option value="'+ item.id + '">' + item.wzxmmc + '</option>';
                projectTableHtml += '<tr> <td>' + item.wzxmmc + '</td><td><input type="button" value="删除" class="del-btn" data-index="' + item.id + '"></input></td></tr>';
            })
        }
        $('#selectGroup').html(selectGroupHtml);
        $('#project-table tbody').html(projectTableHtml);
    })
}
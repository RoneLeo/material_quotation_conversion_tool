/**
 * Created by liurong on 2019/2/13.
 */
console.log(document.getElementsByTagName('body'))
// var ServerUrl = location.origin;
// var ServerUrl = 'http://182.151.22.247:9007'
var ServerUrl = 'http://192.168.0.165:9007'
var selectedId, discountNum, xmmc, file;
$(function () {
    let user = JSON.parse(localStorage.getItem("user"));
    console.log(user)
    if (!user) {
        window.location.href = 'login.html';
    } else {
        getAllProject();
        getAllProvince();
        $('#countBtn').click(function () {
            let value = $('#rate').val();
            if (value == '') {
                value = 0;
            }
            if (value == 0) {
                $('#price').text(0);
            }
            discountNum = parseFloat(value);
            if (discountNum && selectedId) {
                getTotalNum();
                getExportFile();
            }
        })
        $('#newFile').on('change', function (e) {
            let name = $('#newName')[0].value;
            if (e.target.files.length) {
                xmmc = name;
                file = e.target.files[0];
                importProject();
            }
        })
        $('#project-table').on('click', 'input', function (e) {
            // e.preventDefult();
            var id = parseInt($(this).attr('data-index'));
            $.get(ServerUrl + '/project/del?id=' + id, function (data) {
                getAllProject();
                // console.log(data);
            })
            // console.log(e.target.getAttribute("data-index"))
        })
    }
});


function getTotalNum() {
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
            obj.outerHTML = obj.outerHTML;
        },
        error: function () {
            alert("请求数据异常！");
        }
    });
}

function getExportFile() {
    $('#discount-href').attr("href", ServerUrl + '/conbersiontool/getexcelnew?xmbh=' + selectedId + '&discount=' + discountNum + '&lx=0')
    $('#baseprice-href').attr("href", ServerUrl + '/conbersiontool/getexcelnew?xmbh=' + selectedId + '&discount=' + discountNum + '&lx=1');
    $('#cost-href').attr("href", ServerUrl + '/conbersiontool/getexcelnew?xmbh=' + selectedId + '&discount=' + discountNum + '&lx=2');

}

function projectChange() {
    var objS = document.getElementById("selectGroup");
    var projectId = objS.options[objS.selectedIndex].value;
    selectedId = projectId;
    if (discountNum && selectedId) {
        getTotalNum();
        getExportFile();
    }
}

function provinceChange() {
    // var objS = document.getElementById("selectGroup");
    // var projectId = objS.options[objS.selectedIndex].value;
    // selectedId = projectId;
    // if (discountNum && selectedId) {
    //     getTotalNum();
    //     getExportFile();
    // }
}

function getAllProvince() {


    $.get(ServerUrl + '/province/findAll', function (data) {
        var provinces = data.data;
        var selectProvinceHtml = '';
        // var projectTableHtml = '';
        if (provinces && provinces.length) {
            provinces.forEach(function (item) {
                selectProvinceHtml += '<option value="' + item.id + '">' + item.mc + '</option>';
                // projectTableHtml += '<tr> <td>' + item.mc + '</td><td><input type="button" value="删除" class="del-btn" data-index="' + item.id + '"></input></td></tr>';
            })
        }
        $('#selectProvince').html(selectProvinceHtml);
        // $('#project-table tbody').html(projectTableHtml);
    })
}

function getAllProject() {
    $.ajax({
        type: "GET",
        url: ServerUrl + '/project/findAll',
        contentType: "application/json; charset=utf-8",
        dataType: 'json',
        xhrFields: {
            withCredentials: true
        },
        crossDomain: true,
        success: function (data) {
            var projects = data.data;
            var selectGroupHtml = '';
            var projectTableHtml = '';
            if (projects && projects.length) {
                projects.forEach(function (item) {
                    selectGroupHtml += '<option value="' + item.id + '">' + item.wzxmmc + '</option>';
                    projectTableHtml += '<tr> <td>' + item.wzxmmc + '</td><td><input type="button" value="删除" class="del-btn" data-index="' + item.id + '"></input></td></tr>';
                })
            }
            $('#selectGroup').html(selectGroupHtml);
            $('#project-table tbody').html(projectTableHtml);
        }
    });
    // $.get(ServerUrl + '/project/findAll', function (data) {
    //     var projects = data.data;
    //     var selectGroupHtml = '';
    //     var projectTableHtml = '';
    //     if (projects && projects.length) {
    //         projects.forEach(function (item) {
    //             selectGroupHtml += '<option value="' + item.id + '">' + item.wzxmmc + '</option>';
    //             projectTableHtml += '<tr> <td>' + item.wzxmmc + '</td><td><input type="button" value="删除" class="del-btn" data-index="' + item.id + '"></input></td></tr>';
    //         })
    //     }
    //     $('#selectGroup').html(selectGroupHtml);
    //     $('#project-table tbody').html(projectTableHtml);
    // })
}
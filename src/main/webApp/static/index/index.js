var row;
$(function () {
    $("#tableData").bootstrapTable({
        url : "/queryJob",
        clickToSelect: true,    	//是否启用点击选中行
        columns: [
            {field: 'selectItem', radio: true},
            {title: '序号',align: 'center', width:50, formatter:function (value,row,index) {
                    return index+1;
                }
            },
            {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
            {title: '任务名称', field: 'jobName', visible: true, align: 'center', valign: 'middle'},
            //{title: '任务所在组', field: 'jobGroup', visible: true, align: 'center', valign: 'middle'},
            {title: '任务类名', field: 'clazzPath', visible: true, align: 'center', valign: 'middle'},
            {title: '表达式', field: 'cron', visible: true, align: 'center', valign: 'middle'},
            //{title: '触发器所在组', field: 'triggerGroup', visible: false, align: 'center', valign: 'middle'},
            {title: '描述', field: 'jobDesc', visible: false, align: 'center', valign: 'middle'},
            {title: '任务状态', field: 'status', visible: true, align: 'center', valign: 'middle',formatter:function (value) {
                    if (value == 1) {
                        return "运行中"
                    }
                    if (value == 0) {
                        return "已停止"
                    }
                }}
        ]
    })
})
function openInsert() {
    layer.open({
        type: 2,
        title: '添加定时任务',
        area: ['80%', '80%'],
        fixed: false, //不固定
        maxmin: true,
        content: 'view/insert.html',
        success: function (index, e) {
            $(':focus').blur();
        }
    });
}
function check() {
    var selected = $('#tableData').bootstrapTable('getSelections');
    if (selected.length == 0) {
        alert("请先选中表格中的某一记录！");
        return false;
    } else {
        row= selected[0];
        return true;
    }
}
function startJob() {
    if (check()) {
        $.ajax({
            url: '/start_job',
            type: "POST",
            data: {
                id: row.id
            },
            success:function (data) {
                refresh();
                msg(data.msg);
            }
        })
    }
}
function pauseJob() {
    if (check()) {
        $.ajax({
            url: '/pause_job',
            type: "POST",
            data: {
                id: row.id
            },
            success:function (data) {
                refresh();
                msg(data.msg);
            }
        })
    }
}
function deleteJob() {
    if (check()) {
        $.ajax({
            url: '/delete_job',
            type: "POST",
            data: {
                id: row.id
            },
            success:function (data) {
                refresh();
                msg(data.msg);
            }
        })
    }
}
function openUpdate() {
    if (check()) {
        layer.open({
            type: 1,
            title: '修改定时任务',
            area: ['80%', '80%'],
            fixed: false, //不固定
            maxmin: true,
            content: $("#insertWid"),
            success: function (index, e) {
                        $('#id').val(row.id);
                        $('#clazzPath').val(row.clazzPath);
                        $('#jobName').val(row.jobName);
                        $('#cron').val(row.cron);
                $(':focus').blur();
            }
        });
    }
}
function refresh() {
    $("#tableData").bootstrapTable("refresh")
}
function update() {
    $.ajax({
        url : '/update_job',
        type: 'POST',
        data:{
            id : $("#id").val(),
            jobName : $("#jobName").val(),
            cron : $("#cron").val(),
            clazzPath : $("#clazzPath").val()
        },
        success :function (data) {
            refresh();
            back();
            msg(data.msg);
        }
    })
}
function back() {
    layer.closeAll();
}
function msg(msg) {
    layer.msg(msg, {
        time: 2000, //2s后自动关闭
        icon: 1,
     });
}
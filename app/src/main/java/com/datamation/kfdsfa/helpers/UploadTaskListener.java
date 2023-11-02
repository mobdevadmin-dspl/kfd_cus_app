package com.datamation.kfdsfa.helpers;


import com.datamation.kfdsfa.api.TaskTypeUpload;

import java.util.List;

public interface UploadTaskListener {
    void onTaskCompleted(TaskTypeUpload taskType, List<String> list);
    void onTaskCompleted(List<String> list);
}
import React, {useEffect, useState} from "react";
import FormAddSourceBaseURL from "./forms/FormAddSourceBaseURL";
import {useDispatch, useSelector} from "react-redux";
import {addSourceURL, getLabelsBaseURLS, getResourcesWithoutBaseURL, getSourcesURLS} from "../endpointManagementSlice";
import {reset} from "redux-form";

export const AddSourceBaseURL = () => {
    const dispatch = useDispatch();
    const labels = useSelector(getLabelsBaseURLS)

    useEffect( () => {
        dispatch(getResourcesWithoutBaseURL())
    }, [])

    const submit = async (values) => {
        let formData = new FormData(document.forms.namedItem("addSourceBaseURLForm"))
        dispatch(reset("AddSourceBaseURLForm"));
        await dispatch(addSourceURL(formData))
        dispatch(getResourcesWithoutBaseURL())
        dispatch(getSourcesURLS())
    }

    return (
        <div>
            <FormAddSourceBaseURL onSubmit={submit} labels={labels}/>
        </div>
    )
}
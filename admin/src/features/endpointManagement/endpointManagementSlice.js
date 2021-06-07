import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import API from "../../api/Api";


const initialState = {
    sources: []
}


export const getSourcesURLS = createAsyncThunk('endpointManagement/getSourcesURLS', async () => {
    return API.GET("getSourcesURLS", '', [] ).then(res => {
        const results = res.data
        return {results}
    })
})


export const addSourceURL = createAsyncThunk('endpointManagement/addSourceBaseURL', async (form) => {
    return API.GET("addSourceBaseURL", '', form ).then(res => {
        const results = res.data
        return {results}
    })
})


export const editSourceURL = createAsyncThunk('endpointManagement/editSourceBaseURL', async (form) => {
    return API.GET("editSourceBaseURL", '', form ).then(res => {
        const results = res.data
        return {results}
    })
})


const endpointManagementSlice = createSlice({
    name: 'endpointManagementURL',
    initialState,
    reducers: {},
    extraReducers: {
        [getSourcesURLS.fulfilled]: (state, action) => {
            state.sources = action.payload.results
        },
    }
})


export const getListSourcesURLS = state => state.endpointManagementURL.sources


export default endpointManagementSlice.reducer
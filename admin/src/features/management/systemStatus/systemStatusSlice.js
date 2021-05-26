import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import API from "../../../api/Api";

const initialState = {
    allResources: []
}


export const getAllResources = createAsyncThunk('listResources/getAllResources', async () => {
    return API.GET("getAllResources", '', [] ).then(res => {
        const allResources = res.data
        return {allResources}
    })
})


export const startSystemBuild = createAsyncThunk('listResources/startBuildSystem', async () => {
    return API.GET("startBuildSystem", '', [] ).then(res => {
        return res.data
    })
})


const systemStatusSlice = createSlice({
    name: 'systemStatus',
    initialState,
    reducers: {},
    extraReducers: {
        [getAllResources.fulfilled]: (state, action) => {
            state.allResources = action.payload.allResources
        },
    }
})

export const getResources = state => state.systemStatus.allResources

export default systemStatusSlice.reducer
import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import API from "../../../api/Api";

const initialState = {
    allResources: [],
    systemBuild: false
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


export const startUnbuildSystem = createAsyncThunk('listResources/startUnbuildSystem', async () => {
    return API.GET("startUnbuildSystem", '', [] ).then(res => {
        return res.data
    })
})


export const getSystemBuild = createAsyncThunk('listResources/getSystemBuild', async () => {
    return API.GET("getSystemBuild", '', [] ).then(res => {
        console.log("Get System Build: ")
        console.log(res.data)
        const build = res.data
        return {build}
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
        [getSystemBuild.fulfilled]: (state, action) => {
            state.systemBuild = action.payload.build
        },
    }
})


export const getResources = state => state.systemStatus.allResources
export const getSystemBuildValue = state => state.systemStatus.systemBuild


export default systemStatusSlice.reducer
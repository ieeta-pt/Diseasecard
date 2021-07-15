import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import API from "../../api/Api";


const initialState = {
    disease: [],
    network: [],
    listOfIds: [],
    omim: '',
    lastOmim: '',
    status: '',
    ready: '',
    error: null,
    showFrame: "graph",
    url: '',
    concepts: [],
    tree: [],
}

export const getDiseaseByOMIM = createAsyncThunk('disease/getDiseaseByOMIM', async (omim) => {
    return API.GET("diseaseByOMIM", "", [omim] ).then(res => {
        // Organize data from different sources and concat their values

        let values = {};
        const data = res.data

        const network = data.network
        network.push("OMIM:" + data.omim)

        for (const connection in network) {
            const info = network[connection].split(/:(.+)/)

            if (info.length > 1) {
                if (!(info[0] in values)) values[info[0]] = []
                values[info[0]].push(info[1].split(":").join("_"))
            }
        }

        let results = []
        let id = 1
        let listOfIds = ['root']
        let aux = ''
        for (const [ key, value ] of Object.entries(values)) {
            if (value !== "") {
                let children = []
                for ( const index in value ) {
                    aux = key + ":" + value[index];
                    listOfIds.push(aux)
                    if (value[index].length >= 8)   children.push( { "id": aux , "fullName": value[index], "name" : value[index].substring(0,7) + "...", "value":1, "color":"#fc9879" } )
                    else                            children.push( { "id": aux , "fullName": value[index], "name" : value[index], "value":1, "color":"#fc9879" } )
                }
                if (key==='omim') results.push( { "id": id.toString(), "fullName":'OMIM', "name": 'OMIM', "children":children, "color":"#e4582d" } )
                else results.push( { "id": id.toString(), "fullName":key, "name": key, "children": children, "color":"#e4582d" } )
                listOfIds.push(id.toString());
                id++;
            }
        }
        return {data, results, listOfIds};
    })
})

export const getSourceURL = createAsyncThunk('disease/getSourceURL', async (url) => {
    return API.GET("sourceURL", url, [] ).then(res => {
        let url = res.data.url
        let protection = res.data.protection
        return { url, protection };
    })
})

export const getInitialTreeStructure = createAsyncThunk('disease/treeStructure', async (url) => {
    return API.GET("treeStructure", "", [] ).then(res => {
        let concepts = res.data
        let tree = []
        concepts.forEach((value) => tree.push(value.replace("concept_", "")))

        return { tree };
    })
})

const diseaseSlice = createSlice({
    name: 'disease',
    initialState,
    reducers: {
        showFrame: (state, action) => {
            state.showFrame = action.payload
        }
    },
    extraReducers: {
        [getDiseaseByOMIM.pending]: (state, action) => {
            state.status = 'loading'
            state.network = []
            state.omim = action.meta.arg
        },
        [getDiseaseByOMIM.fulfilled]: (state, action) => {
            state.status = 'succeeded'
            state.disease = action.payload.data
            state.network = action.payload.results
            state.omim = action.meta.arg
            state.listOfIds = action.payload.listOfIds
            state.ready = 'go'

            state.tree = []
            state.concepts.forEach(function (key, i) {
                let bool = true;
                for (const elem in state.network) {
                    if (state.network[elem].name === key) {
                        bool = false;
                        state.tree.push( { "id": state.network[elem].id, "fullName":key, "name": key, "children": state.network[elem].children}    )
                    }
                }
                if (bool) state.tree.push({ "id": i.toString()+"_", "fullName":key, "name": key, "children": [] })
            })
            state.lastOmim = state.omim
        },
        [getDiseaseByOMIM.rejected]: (state, action) => {
            state.status = 'failed'
            state.omim = action.meta.arg
            state.error = action.error.message
        },

        [getSourceURL.pending]: (state, action) => {
            //state.status = 'loading'
        },
        [getSourceURL.fulfilled]: (state, action) => {
            state.url = action.payload.url
            state.protection = action.payload.protection
            //state.status = 'succeeded'
        },
        [getSourceURL.rejected]: (state, action) => {
            //state.status = 'failed'
            state.omim = action.meta.arg
            state.error = action.error.message
        },

        [getInitialTreeStructure.fulfilled]: (state, action) => {
            state.concepts = action.payload.tree
        }
    }
})


export const selectOMIM = state => state.disease.omim
export const selectLastOMIM = state => state.disease.lastOmim
export const selectNetwork = state => state.disease.network
export const selectTree = state => state.disease.tree
export const getStatus = state => state.disease.status
export const getReady = state => state.disease.ready
export const getDescription = state => state.disease.disease.description
export const getListOfIds = state => state.disease.listOfIds
export const getURL= state => state.disease.url
export const getProtection= state => state.disease.protection
export const showFrameSource = state => state.disease.showFrame

export const { showFrame } = diseaseSlice.actions

export default diseaseSlice.reducer

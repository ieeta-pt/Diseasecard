import { createSlice } from '@reduxjs/toolkit'
import config from "./config";

const initialState = {
    isOpen: [],
    isTrigger: [],
    ...config,
    isFullScreen: false,
}

let trigger = [];
let open = [];

const templateSlice = createSlice({
    name: 'template',
    initialState,

    reducers: {
        collapse_menu: (state, action) => {
            return {
                ...state,
                collapseMenu: !state.collapseMenu
            };
        },
        collapse_toggle: (state, action) => {
            //console.log(action)
            if (action.payload.type === 'sub') {
                open = state.isOpen;
                trigger = state.isTrigger;

                const triggerIndex = trigger.indexOf(action.payload.id);
                if (triggerIndex > -1) {
                    open = open.filter(item => item !== action.payload.id);
                    trigger = trigger.filter(item => item !== action.payload.id);
                }

                if (triggerIndex === -1) {
                    open = [...open, action.payload.id];
                    trigger = [...trigger, action.payload.id];
                }
            } else {
                open = state.isOpen;
                const triggerIndex = (state.isTrigger).indexOf(action.payload.id);
                trigger = (triggerIndex === -1) ? [action.payload.id] : [];
                open = (triggerIndex === -1) ? [action.payload.id] : [];
            }

            return {
                ...state,
                isOpen: open,
                isTrigger: trigger
            };
        },
        nav_content_leave: (state, action) => {
            return {
                ...state,
                isOpen: open,
                isTrigger: trigger,
            };
        },
        nav_collapse_leave: (state, action) => {
            if (action.payload.type === 'sub') {
                open = state.isOpen;
                trigger = state.isTrigger;

                const triggerIndex = trigger.indexOf(action.payload.id);
                if (triggerIndex > -1) {
                    open = open.filter(item => item !== action.payload.id);
                    trigger = trigger.filter(item => item !== action.payload.id);
                }
                return {
                    ...state,
                    isOpen: open,
                    isTrigger: trigger,
                };
            }
            return {...state};
        },
        full_screen: (state, action) => {
            return {
                ...state,
                isFullScreen: !state.isFullScreen
            };
        },
        full_screen_exit: (state, action) => {
            return {
                ...state,
                isFullScreen: false
            };
        },
        change_layout: (state, action) => {
            // TODO: VERIFICAR SE É action.layout OU action.payload.layout
            return {
                ...state,
                layout: action.layout
            };
        }
    },
    extraReducers: {}
})

export const getIsOpen = state => state.template.isOpen
export const getIsTrigger = state => state.template.isTrigger

export const { full_screen_exit, collapse_menu, full_screen, collapse_toggle, nav_collapse_leave, nav_content_leave, change_layout } = templateSlice.actions
export default templateSlice.reducer
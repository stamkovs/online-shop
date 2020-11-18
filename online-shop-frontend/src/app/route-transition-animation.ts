import {animate, animateChild, group, query, style, transition, trigger} from '@angular/animations';

export const routeTransitionAnimations = trigger('triggerName', [
  transition('One => Two, One => Three, One => Four, Two => Three, Two => Four, Three => Four', [
    style({ position: 'absolute', overflow: 'hidden', height: 'calc(100% - 64px)' }),
    query(':enter, :leave', [
      style({
        position: 'absolute',
        top: 0,
        right: 0,
        width: '100%',
        height: '100%',
        overflow: 'hidden'
      })
    ]),
    query(':enter', [style({ right: '-100%' })]),
    query(':leave', animateChild()),
    group([
      query(':leave', [animate('.75s ease-out', style({ right: '100%' }))]),
      query(':enter', [animate('.75s ease-out', style({ right: '0%' }))])
    ]),
    query(':enter', animateChild())
  ]),
  transition('Four => Three, Four => Two, Four => One, Three => Two, Three => One, Two => One', [
    style({ position: 'absolute', overflow: 'hidden', height: 'calc(100% - 64px)' }),
    query(':enter, :leave', [
      style({
        position: 'absolute',
        top: 0,
        right: 0,
        width: '100%',
        height: '100%',
        overflow: 'hidden'
      })
    ]),
    query(':enter', [style({ left: '-100%' })]),
    query(':leave', animateChild()),
    group([
      query(':leave', [animate('.75s ease-out', style({ left: '100%' }))]),
      query(':enter', [animate('.75s ease-out', style({ left: '0%' }))])
    ]),
    query(':enter', animateChild())
  ])
]);

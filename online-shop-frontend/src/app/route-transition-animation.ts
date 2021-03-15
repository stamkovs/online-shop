import {animate, animateChild, group, query, style, transition, trigger} from '@angular/animations';

export const routeTransitionAnimations = trigger('triggerName', [
  transition('One => Two, One => Three, One => Four, One => Five, One => Six, Two => Three, Two => Four,' +
    'Two => Five, Two => Six, Three => Four, Three => Five, Three => Six, Four => Five, Four => Six, Five => Six', [
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
    query(':enter', [style({ right: '-100%', opacity: 1 })]),
    query(':leave', animateChild()),
    group([
      query(':leave', [animate('.6s ease-out', style({ right: '100%', opacity: 0 }))]),
      query(':enter', [animate('.6s ease-out', style({ right: '0%', opacity: 1 }))])
    ]),
    query(':enter', animateChild())
  ]),
  transition('Six => Five, Six => Four, Six => Three, Six => Two, Six => One, Five => Four, Five => Three,' +
    ' Five => Two, Five => One, Four => Three, Four => Two, Four => One, Three => Two, Three => One, Two => One', [
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
    query(':enter', [style({ left: '-100%', opacity: 1 })]),
    query(':leave', animateChild()),
    group([
      query(':leave', [animate('.6s ease-out', style({ left: '100%', opacity: 0 }))]),
      query(':enter', [animate('.6s ease-out', style({ left: '0%', opacity: 1 }))])
    ]),
    query(':enter', animateChild())
  ])
]);
